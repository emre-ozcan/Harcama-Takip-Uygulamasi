package com.emreozcan.harcamalaruygulamasi.model


import com.google.gson.annotations.SerializedName

/**
 * Gelen veriye göre oluşturulmuş model sınıfıdır
 * Model oluşturulmak için hazır bazı uygulamalar vardır. Sizde Android Studionuza kurulumunu gerçekleştirebilirsiniz
 *
 */
data class EURCurrency(
    @SerializedName("EUR_TRY")
    val eURTRY: Double
)