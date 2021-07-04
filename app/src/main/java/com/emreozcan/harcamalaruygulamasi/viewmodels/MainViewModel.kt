package com.emreozcan.harcamalaruygulamasi.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.emreozcan.harcamalaruygulamasi.data.DataStoreRepository
import com.emreozcan.harcamalaruygulamasi.data.Repository
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.model.EURCurrency
import com.emreozcan.harcamalaruygulamasi.model.GBPCurrency
import com.emreozcan.harcamalaruygulamasi.model.USDCurrency
import com.emreozcan.harcamalaruygulamasi.util.Constants
import com.emreozcan.harcamalaruygulamasi.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    /**DataStoreRepository*/
    val readDollar = dataStoreRepository.readDollarRate.asLiveData()
    val readEuro = dataStoreRepository.readEuroRate.asLiveData()
    val readSterlin = dataStoreRepository.readGbpRate.asLiveData()

    val readNameAndGender = dataStoreRepository.readGenderAndName.asLiveData()
    val readCurrentCurrency = dataStoreRepository.readCurrentCurrency.asLiveData()
    val readCurrentCurrencyId = dataStoreRepository.readCurrentCurrencyId.asLiveData()

    val readOnboarding = dataStoreRepository.readOnboarding.asLiveData()

    fun saveNameAndGender(name: String, gender: String, genderId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveGenderAndName(gender,name,genderId)
        }
    }

    fun saveCurrentCurrency(string: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveCurrentCurrency(string)
        }
    }

    fun saveCurrentCurrencyId(int: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveCurrentCurrencyId(int)
        }
    }

    fun saveOnboarding(boolean: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveOnboarding(boolean)
        }
    }

    /**Room*/
    val readHarcamalar: LiveData<List<HarcamalarEntity>> = repository.local.readExpences().asLiveData()

     fun insertExpence(harcamalarEntity: HarcamalarEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertExpence(harcamalarEntity)
        }

    fun deleteExpence(harcamalarEntity: HarcamalarEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteExpence(harcamalarEntity)
        }

    /**Retrofit*/
    var usdResponse: MutableLiveData<NetworkResult<USDCurrency>> = MutableLiveData()
    var euroResponse: MutableLiveData<NetworkResult<EURCurrency>> = MutableLiveData()
    var sterlinResponse: MutableLiveData<NetworkResult<GBPCurrency>> = MutableLiveData()

    fun getCurrencies() = viewModelScope.launch {
        getCurrencySafeCall(applyQueries("USD_TRY"),usdResponse,"usd")
        getCurrencySafeCall(applyQueries("EUR_TRY"),euroResponse,"euro")
        getCurrencySafeCall(applyQueries("GBP_TRY"),sterlinResponse,"sterlin")
    }

    private suspend fun <T> getCurrencySafeCall(
        queries: Map<String, String>,
        response: MutableLiveData<NetworkResult<T>>,
        currency: String
    ) {

        response.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val tempResponse = when (currency) {
                    "usd" -> repository.remote.getUSD(queries)
                    "euro" -> repository.remote.getEURO(queries)
                    "sterlin" -> repository.remote.getGBP(queries)
                    else -> {
                        return
                    }
                }

                response.value = handleCurrencyResponse(tempResponse as Response<T>)

                if (response.value!!.data != null){
                    println(response.value!!.data)
                    when(currency){
                        "usd" ->{
                            val usdCurrency: USDCurrency = response.value!!.data as USDCurrency
                            saveRate(currency, usdCurrency.dolarRate)
                        }
                        "euro" -> {
                            val euroCurrency: EURCurrency = response.value!!.data as EURCurrency
                            saveRate(currency, euroCurrency.euroRate)
                        }
                        "sterlin" ->{
                            val gbpCurrency : GBPCurrency = response.value!!.data  as GBPCurrency
                            saveRate(currency, gbpCurrency.sterlinRate)
                        }
                    }

                }
            } catch (e: Exception) {
                response.value = NetworkResult.Error("Hata !")
            }
        }else{
            response.value = NetworkResult.Error("İnternet Bağlantısı Yok")
        }

    }

    private fun <T> handleCurrencyResponse(response: Response<T>): NetworkResult<T> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun saveRate(whichRate: String, rate: Double){
        viewModelScope.launch(Dispatchers.IO) {
            when(whichRate){
                "usd" -> dataStoreRepository.saveDollarRate(rate)
                "euro" -> dataStoreRepository.saveEuroRate(rate)
                "sterlin" -> dataStoreRepository.saveGbpRate(rate)
            }
        }
    }

    private fun applyQueries(rate: String): HashMap<String,String>{
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_CURRENCY_TYPE] = rate
        queries[Constants.QUERY_COMPACT] = Constants.QUERY_ULTRA
        queries[Constants.QUERY_API] = Constants.API_KEY

        return queries
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}