package com.emreozcan.harcamalaruygulamasi.model


import com.google.gson.annotations.SerializedName
/**
 * Gelen veriye göre oluşturulmuş model sınıfıdır
 */
data class USDCurrency(
    @SerializedName("USD_TRY")
    val uSDTRY: Double
)