package com.emreozcan.harcamalaruygulamasi.model

import com.google.gson.annotations.SerializedName

data class EURCurrency(
    @SerializedName("EUR_TRY")
    val euroRate: Double
)

data class GBPCurrency(
    @SerializedName("GBP_TRY")
    val sterlinRate: Double
)

data class USDCurrency(
    @SerializedName("USD_TRY")
    val dolarRate: Double
)
