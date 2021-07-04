package com.emreozcan.harcamalaruygulamasi.util

class Constants {

    companion object{

        /**Retrofit*/
        const val BASE_URL = "https://free.currconv.com"
        const val API_KEY = "3cd883945f8230027fff"
        const val QUERY_CURRENCY_TYPE = "q"
        const val QUERY_COMPACT = "compact"
        const val QUERY_API = "apiKey"
        const val QUERY_ULTRA = "ultra"


        /**Room*/
        const val EXPENSE_TABLE_NAME = "harcamalar"

        /**DataStore*/
        const val DATASTORE_PREFERENCE_NAME = "exchange_rate_preferences"
        const val DOLLAR_PREFERENCE_KEY = "dollar_preference"
        const val EURO_PREFERENCE_KEY = "euro_preference"
        const val GBP_PREFERENCE_KEY = "gbp_preference"
        const val GENDER_PREFERENCE_KEY = "gender_preference"
        const val NAME_PREFERENCE_KEY = "name_preference"
        const val GENDER_ID_PREFERENCE_KEY = "gender_id_preference"
        const val CURRENT_CURRENCY_PREFERENCE_KEY = "current_currency_preference"
        const val CURRENT_CURRENCY_ID_PREFERENCE_KEY = "current_currency_id_preference"
        const val ONBOARDING_PREFERENCE_KEY = "onboarding_preference"

    }
}