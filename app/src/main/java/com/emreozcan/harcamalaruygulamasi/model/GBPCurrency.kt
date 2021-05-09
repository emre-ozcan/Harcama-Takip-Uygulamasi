package com.emreozcan.harcamalaruygulamasi.model


import com.google.gson.annotations.SerializedName
/**
 * Gelen veriye göre oluşturulmuş model sınıfıdır
 */
data class GBPCurrency(
    @SerializedName("GBP_TRY")
    val gBPTRY: Double
)