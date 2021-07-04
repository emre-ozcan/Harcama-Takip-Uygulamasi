package com.emreozcan.harcamalaruygulamasi.data

import com.emreozcan.harcamalaruygulamasi.data.network.CurrencyAPI
import com.emreozcan.harcamalaruygulamasi.model.EURCurrency
import com.emreozcan.harcamalaruygulamasi.model.GBPCurrency
import com.emreozcan.harcamalaruygulamasi.model.USDCurrency
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val currencyAPI: CurrencyAPI
) {

    suspend fun getUSD(queries: Map<String,String>) : Response<USDCurrency>{
        return currencyAPI.getUSD(queries)
    }

    suspend fun getEURO(queries: Map<String,String>) : Response<EURCurrency>{
        return currencyAPI.getEURO(queries)
    }

    suspend fun getGBP(queries: Map<String, String>) : Response<GBPCurrency>{
        return currencyAPI.getGBP(queries)
    }


}