package com.emreozcan.harcamalaruygulamasi.data.network

import com.emreozcan.harcamalaruygulamasi.model.EURCurrency
import com.emreozcan.harcamalaruygulamasi.model.GBPCurrency
import com.emreozcan.harcamalaruygulamasi.model.USDCurrency
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * https://free.currconv.com/api/v7/convert?q=USD_TRY&compact=ultra&apiKey=3cd883945f8230027fff
 * Yukarıda gösterilen bize dolar kurunu vermektedir. İncelediğimiz zamanda anlaşılacağı gibi.
 * Ayrıca apiKey kısmı vardır burada ise bu apinin size özel ürettiği apiKey'i girmeniz gereklidir!
 * Şu anda gördüğünüz benim keyimdir. Kullanılan api saatlik 100 isteğe izin vermektedir. Proje
 * açık olduğundan dolayı eğer çalışmazsa kendinize özel bir key oluşturmanız gerekli internet sitesinden
 * sonrasında bu keyi alt tarafta bulunan kısıma yazmanız gereklidir! Görüldüğü üzere linkin belli bir kısmı
 * eksiktir bu eksik olan kısmın ismi BASE_URL dir bunun tanımlaması Giriş Fragmentta yapılmıştır
 */

/**
 *  Kullanılan API -> https://free.currencyconverterapi.com/
 *  @GET("/api/v7/convert?q=USD_TRY&compact=ultra&apiKey=3cd883945f8230027fff")
 */


interface CurrencyAPI {
    /**
    API aynı anda 2 den fazla para birimi sorgusu kabul etmemektedir bu yüzden ayrı ayrı
    veri çekilmesi gerekmektedir
     */

    @GET("/api/v7/convert")
    suspend fun getUSD(@QueryMap queries: Map<String, String>): Response<USDCurrency>

    @GET("/api/v7/convert")
    suspend fun getEURO(@QueryMap queries: Map<String, String>): Response<EURCurrency>

    @GET("/api/v7/convert")
    suspend fun getGBP(@QueryMap queries: Map<String, String>): Response<GBPCurrency>

}