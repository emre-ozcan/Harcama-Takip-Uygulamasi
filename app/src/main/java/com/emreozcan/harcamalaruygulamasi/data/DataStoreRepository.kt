package com.emreozcan.harcamalaruygulamasi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.CURRENT_CURRENCY_ID_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.CURRENT_CURRENCY_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.DATASTORE_PREFERENCE_NAME
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.DOLLAR_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.EURO_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.GBP_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.GENDER_ID_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.GENDER_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.NAME_PREFERENCE_KEY
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.ONBOARDING_PREFERENCE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {

        val rateDollar = doublePreferencesKey(DOLLAR_PREFERENCE_KEY)
        val rateEuro = doublePreferencesKey(EURO_PREFERENCE_KEY)
        val rateGbp = doublePreferencesKey(GBP_PREFERENCE_KEY)

        val gender = stringPreferencesKey(GENDER_PREFERENCE_KEY)
        val name = stringPreferencesKey(NAME_PREFERENCE_KEY)
        val genderId = intPreferencesKey(GENDER_ID_PREFERENCE_KEY)

        val currentCurrency = stringPreferencesKey(CURRENT_CURRENCY_PREFERENCE_KEY)
        val currentCurrencyId = intPreferencesKey(CURRENT_CURRENCY_ID_PREFERENCE_KEY)

        val onboarding = booleanPreferencesKey(ONBOARDING_PREFERENCE_KEY)

    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        DATASTORE_PREFERENCE_NAME
    )


    /**Save Data*/
    suspend fun saveDollarRate(rate: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.rateDollar] = rate
        }
    }

    suspend fun saveEuroRate(rate: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.rateEuro] = rate
        }
    }

    suspend fun saveGbpRate(rate: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.rateGbp] = rate
        }
    }

    suspend fun saveGenderAndName(gender: String, name: String, genderId: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.gender] = gender
            preferences[PreferenceKeys.name] = name
            preferences[PreferenceKeys.genderId] = genderId

        }
    }


    suspend fun saveCurrentCurrency(string: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.currentCurrency] = string
        }
    }

    suspend fun saveCurrentCurrencyId(int: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.currentCurrencyId] = int
        }
    }

    suspend fun saveOnboarding(boolean: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.onboarding] = boolean
        }
    }

    /**Read Data*/
    val readDollarRate: Flow<Double> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val dollarRate = preferences[PreferenceKeys.rateDollar] ?: 0.0

            dollarRate
        }

    val readEuroRate: Flow<Double> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val euroRate = preferences[PreferenceKeys.rateEuro] ?: 0.0

            euroRate
        }

    val readGbpRate: Flow<Double> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val gbpRate = preferences[PreferenceKeys.rateGbp] ?: 0.0
            gbpRate
        }

    val readGenderAndName: Flow<GenderAndName> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val gender = preferences[PreferenceKeys.gender] ?: "none"
            val name = preferences[PreferenceKeys.name] ?: "none"
            val genderId = preferences[PreferenceKeys.genderId] ?: 0

            GenderAndName(gender, name, genderId)
        }

    val readCurrentCurrency: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val currency = preferences[PreferenceKeys.currentCurrency] ?: "₺"
            currency
        }

    val readCurrentCurrencyId: Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val currencyId = preferences[PreferenceKeys.currentCurrencyId] ?: 0
            currencyId
        }
    val readOnboarding: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val onboarding = preferences[PreferenceKeys.onboarding] ?: false
            onboarding
        }


}

data class GenderAndName(
    val gender: String,
    val name: String,
    val genderId: Int
)