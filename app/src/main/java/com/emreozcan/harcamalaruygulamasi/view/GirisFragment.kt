package com.emreozcan.harcamalaruygulamasi.view

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.harcamalaruygulamasi.*
import com.emreozcan.harcamalaruygulamasi.adapter.RecyclerCardAdapter
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentGirisBinding
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar
import com.emreozcan.harcamalaruygulamasi.db.Veritabani
import com.emreozcan.harcamalaruygulamasi.model.EURCurrency
import com.emreozcan.harcamalaruygulamasi.model.GBPCurrency
import com.emreozcan.harcamalaruygulamasi.model.USDCurrency
import com.emreozcan.harcamalaruygulamasi.service.CurrencyAPI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_giris.*
import kotlinx.android.synthetic.main.fragment_giris.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


class GirisFragment : Fragment() , RecyclerCardAdapter.Listener {
    private lateinit var harcamaList : ArrayList<Harcamalar>
    private lateinit var db : Veritabani

    private val BASE_URL = "https://free.currconv.com"

    //BASE_URL burada tanımlanır ve bir çok yerden erişme imkanı sağlar ayrıca retrofiti tanımlarken kullanırız

    private lateinit var recyclerCardAdapter : RecyclerCardAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var isim : String
    private lateinit var cinsiyet : String
    private lateinit var lastCurrency : String

    private var _binding: FragmentGirisBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("com.emreozcan.harcamalaruygulamasi", Context.MODE_PRIVATE)!!
        editor = sharedPreferences?.edit()
        isim = sharedPreferences?.getString("isim","yok")!!
        cinsiyet = sharedPreferences?.getString("cinsiyet","yok")!!
        db = Veritabani.ornegiGetir(requireContext())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGirisBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.GONE

        lastCurrency = sharedPreferences?.getString("lastCurrency","yok")!!

        if (cinsiyet=="Erkek"){
            binding.textViewIsim.text = "$isim Bey"
        }else if(cinsiyet=="Kadın"){
            binding.textViewIsim.text = "$isim Hanım"
        }else if (cinsiyet=="none"){
            binding.textViewIsim.text = "$isim"
        }else{
            binding.textViewIsim.text = "Tıklayın"
        }

        hideFab() //Floating Action Buttonu gizlemek için oluşturulmuştur

        harcamaList = ArrayList(db.harcamalarDao.getAll())
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        if (!isOnline(requireContext())){
            Snackbar.make(view,"İnternete Bağlı Değilsiniz !",Snackbar.LENGTH_SHORT).setAction("TAMAM",
                View.OnClickListener {  }).show()
        }

        if (lastCurrency=="lastTL"||lastCurrency=="yok"){
            loadTL()
        }else if(lastCurrency=="lastDolar"){
            loadUSD()
        }else if (lastCurrency=="lastEuro"){
            loadEUR()
        }else if (lastCurrency=="lastSterlin"){
            loadGBP()
        }

        binding.fab.setOnClickListener(View.OnClickListener {
            val action = GirisFragmentDirections.actionGirisFragmentToEkleFragment()
            Navigation.findNavController(it).navigate(action)
        })

        binding.textViewIsim.setOnClickListener(View.OnClickListener {
            //Android Studio 4.2 güncellemesinden sonra bu kısımda hata var gibi gösteriyor fakat hatasız şekilde çalışmaktadır
            val action = GirisFragmentDirections.actionGirisFragmentToİsimFragment()//Sorun yoktur çalıştırabilirsiniz
            view.findNavController().navigate(action)
        })

        binding.buttonTL.setOnClickListener(View.OnClickListener {
            loadTL()
        })
        binding.buttonDolar.setOnClickListener(View.OnClickListener {

            loadUSD()
        })
        binding.buttonEuro.setOnClickListener(View.OnClickListener {

            loadEUR()
        })
        binding.buttonSterlin.setOnClickListener(View.OnClickListener {

            loadGBP()
        })

    }

    override fun cardItemClick(harcama: Harcamalar, currency: String) {
        val action = GirisFragmentDirections.actionGirisFragmentToDetayFragment(harcama,currency)
        view?.findNavController()?.navigate(action)
    }
    fun changeButton(buttonChange : Button, button1 : Button,button2: Button,button3:Button){
        /**
         * Bu fonksiyon butana basıldığında diğer butonların değişmesi için oluşturulmuştur
         */
        buttonChange.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
        button1.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_text_color));
        button2.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_text_color));
        button3.setTextColor(ContextCompat.getColor(requireContext(), R.color.button_text_color));
    }
    fun calculateHarcama(harcamaList : ArrayList<Harcamalar>, currency: String){
        var harcama = 0.0
        for (h in harcamaList){
            harcama+= h.harcama!!
        }
        if(harcama==0.0){
            binding.textViewHarcama.text = "Yok"
        }else{
            binding.textViewHarcama.text = "${harcama.roundToInt()} $currency"
            //gelen double veri integer a rountToInt fonksiyonu ile çevrilir
        }
    }
    fun loadUSD(){
        if (!isOnline(requireContext())){
            /**
             * Uygulamamız offline olarakta çalışmaktadır bunun için internet kontrol edilmelidir eğer internet yoksa
             * burada offline çalışır
             */
            val dolar = sharedPreferences.getFloat("dolar",8.3f)
            harcamaList = ArrayList(db.harcamalarDao.getAll())
            for (x in harcamaList) {
                x.harcama = x.harcama?.div(dolar)
            }
            recyclerCardAdapter =
                RecyclerCardAdapter(harcamaList, listener = this@GirisFragment, "$")
            recyclerCardAdapter.notifyDataSetChanged()
            calculateHarcama(harcamaList, "$")
            editor.putFloat("dolar",dolar)
            editor.putString("lastCurrency", "lastDolar")
            editor.apply()

        }else {


            /**
             * Verileri çekerken yüklendiğini belirtmemiz gerekiyor ilk veriyi çekmek istediğimizde progress barı görünür yapıp recyclerı
             * GONE diyerek görünmez yaparız sonrasında veri geldiğinde aynı işlemin tersini yaparız
             */
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.textViewHarcama.text = "..."
            //İnternetten veriyi çeker
            //Görüldüğü üzere BASE_URL burada kullanılmıştır
            //Retrofit kurulumları yapılır
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            //servisimiz tanımlanır
            val service = retrofit.create(CurrencyAPI::class.java)
            val call = service.getUSD() //tanımlanan servisten fonksiyon çağrılır
            call.enqueue(object : Callback<USDCurrency> {
                override fun onResponse(call: Call<USDCurrency>, response: Response<USDCurrency>) {
                    if (response.isSuccessful) {
                        response.let {
                            val usd = it.body()
                            //response bize cevabımızı getirir bunuda değişkene ekleriz

                            harcamaList = ArrayList(db.harcamalarDao.getAll())
                            for (x in harcamaList) {
                                x.harcama = x.harcama?.div(usd!!.uSDTRY)
                            }
                            /**
                             * burada room databasedeki veri çekilir sonrasında foreach döngüsü ile
                             * tek tek elemanların harcama verisine erişilir sonrasında ise gelen veriye bölünür
                             * bu algoritma ile tl verisi dolar a çevirilir
                             */


                            recyclerCardAdapter =
                                RecyclerCardAdapter(harcamaList, listener = this@GirisFragment, "$")
                            //Yaprığımız değişiklikten recycler adapterın haberi olması lazım yoksa çalışmaz

                            recyclerCardAdapter.notifyDataSetChanged()
                            binding.recyclerView.adapter = recyclerCardAdapter

                            calculateHarcama(harcamaList, "$")
                            /**
                             * alınan veri sonrasında kaydedilir bu sayede yeni bir harcama eklediğinde
                             * farklı bir para biriminde ise bu kaydedilmiş veriyi kullanarak değiştirilmesini yapar
                             *
                             * Ayrıca en son basılan para biriminde kalması lazım bu yüzden kontrolümüzü sağlamak için
                             * bir lastCurrency değişkeni oluştururuz ve en son dolara basıldığını kaydederiz
                             * bu sayede sayfa tekrar yüklendiğinde kontrolü sağlamış oluruz.
                             */
                            editor.putFloat("dolar", usd!!.uSDTRY.toFloat())
                            editor.putString("lastCurrency", "lastDolar")
                            editor.apply()
                            //veriler başarılı şekilde geldi progressbarı görünmez yap ve recycler'ı görünür yap deriz
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<USDCurrency>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
        changeButton(buttonDolar, buttonTL, buttonEuro, buttonSterlin)
        //butona basıldığında diğer butonların text rengi değişir
    }
    fun loadEUR(){
        if (!isOnline(requireContext())){
            val euro = sharedPreferences.getFloat("euro",9.99f)
            harcamaList = ArrayList(db.harcamalarDao.getAll())
            for (x in harcamaList) {
                x.harcama = x.harcama?.div(euro)
            }
            recyclerCardAdapter =
                RecyclerCardAdapter(harcamaList, listener = this@GirisFragment, "€")
            recyclerCardAdapter.notifyDataSetChanged()
            binding.recyclerView.adapter = recyclerCardAdapter
            calculateHarcama(harcamaList, "€")
            editor.putFloat("euro", euro)
            editor.putString("lastCurrency", "lastEuro")
            editor.apply()

        }else {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.textViewHarcama.text = "..."

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(CurrencyAPI::class.java)
            val call = service.getEURO()
            call.enqueue(object : Callback<EURCurrency> {
                override fun onResponse(call: Call<EURCurrency>, response: Response<EURCurrency>) {
                    if (response.isSuccessful) {
                        response.let {
                            val eur = it.body()
                            //Log.e("EUR : ", eur!!.eURTRY.toString())
                            harcamaList = ArrayList(db.harcamalarDao.getAll())
                            for (x in harcamaList) {
                                x.harcama = x.harcama?.div(eur!!.eURTRY)
                            }
                            recyclerCardAdapter =
                                RecyclerCardAdapter(harcamaList, listener = this@GirisFragment, "€")
                            recyclerCardAdapter.notifyDataSetChanged()
                            binding.recyclerView.adapter = recyclerCardAdapter
                            calculateHarcama(harcamaList, "€")
                            editor.putFloat("euro", eur!!.eURTRY.toFloat())
                            editor.putString("lastCurrency", "lastEuro")
                            editor.apply()
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<EURCurrency>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
        changeButton(buttonEuro, buttonDolar, buttonTL, buttonSterlin)
    }
    fun loadGBP(){
        if (!isOnline(requireContext())){
            val sterlin = sharedPreferences.getFloat("sterlin",11.57f)
            harcamaList = ArrayList(db.harcamalarDao.getAll())
            for (x in harcamaList){
                x.harcama = x.harcama?.div(sterlin)
            }
            recyclerCardAdapter = RecyclerCardAdapter(harcamaList,listener = this@GirisFragment,"£")
            recyclerCardAdapter.notifyDataSetChanged()

            binding.recyclerView.adapter = recyclerCardAdapter

            calculateHarcama(harcamaList,"£")

            editor.putFloat("sterlin", sterlin)
            editor.putString("lastCurrency", "lastSterlin")
            editor.apply()
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.textViewHarcama.text = "..."

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(CurrencyAPI::class.java)
            val call = service.getGBP()
            call.enqueue(object : Callback<GBPCurrency> {
                override fun onResponse(call: Call<GBPCurrency>, response: Response<GBPCurrency>) {
                    if (response.isSuccessful) {
                        response.let {
                            val gbp = it.body()
                            harcamaList = ArrayList(db.harcamalarDao.getAll())
                            for (x in harcamaList) {
                                x.harcama = x.harcama?.div(gbp!!.gBPTRY)
                            }
                            recyclerCardAdapter =
                                RecyclerCardAdapter(harcamaList, listener = this@GirisFragment, "£")
                            recyclerCardAdapter.notifyDataSetChanged()
                            binding.recyclerView.adapter = recyclerCardAdapter
                            calculateHarcama(harcamaList, "£")
                            editor.putFloat("sterlin", gbp!!.gBPTRY.toFloat())
                            editor.putString("lastCurrency", "lastSterlin")
                            editor.apply()
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<GBPCurrency>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
        changeButton(buttonSterlin, buttonDolar, buttonEuro, buttonTL)
    }
    fun loadTL(){
        changeButton(buttonTL,buttonDolar,buttonEuro,buttonSterlin)

        harcamaList = ArrayList(db.harcamalarDao.getAll())
        recyclerCardAdapter = RecyclerCardAdapter(harcamaList,listener = this@GirisFragment,"TL")
        recyclerCardAdapter.notifyDataSetChanged()
        binding.recyclerView.adapter = recyclerCardAdapter

        calculateHarcama(harcamaList,"TL")

        editor.putString("lastCurrency","lastTL")
        editor.apply()

    }
    private fun hideFab(){
        /**
         * Kullanıcı Recyclerda aşağıya kaydırdığında floating action button ın kaybolmasını sağlar
         * eğer kaybolmazsa en son eklenen veri gözükmeyebilir
         */
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy<0&&!binding.fab.isShown){
                    binding.fab.show()
                }else if (dy>0&&binding.fab.isShown){
                    binding.fab.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
    fun isOnline(context: Context): Boolean {
        /**
         * Bu fonksiyon ile kullanıcının interneti var mı yok mu olduğu anlaşılır
         * buna görede boolean döndürür ve bu sayede kontrollerimizi kolay bir şekilde yapabiliriz
         */
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}