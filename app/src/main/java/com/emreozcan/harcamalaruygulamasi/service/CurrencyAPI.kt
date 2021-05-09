package com.emreozcan.harcamalaruygulamasi.service

import com.emreozcan.harcamalaruygulamasi.model.EURCurrency
import com.emreozcan.harcamalaruygulamasi.model.GBPCurrency
import com.emreozcan.harcamalaruygulamasi.model.USDCurrency
import retrofit2.Call
import retrofit2.http.GET

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
 */


interface CurrencyAPI {

    //apiKeyi değiştirmeyi unutma !
    @GET("/api/v7/convert?q=USD_TRY&compact=ultra&apiKey=3cd883945f8230027fff")
    fun getUSD(): Call<USDCurrency>

    @GET("/api/v7/convert?q=EUR_TRY&compact=ultra&apiKey=3cd883945f8230027fff")
    fun getEURO(): Call<EURCurrency>

    @GET("/api/v7/convert?q=GBP_TRY&compact=ultra&apiKey=3cd883945f8230027fff")
    fun getGBP(): Call<GBPCurrency>
}