package com.emreozcan.harcamalaruygulamasi.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface HarcamalarDao {

    //Bu bizim veritabanı ile iletişim interfacesimizdir. Sorgular yapmamıza olanak tanır ve bu cevapları bize döndürür

    @Query("SELECT * FROM harcamalar")
    fun getAllExpences(): Flow<List<HarcamalarEntity>>

    @Insert
    suspend fun insertExpence(harcama: HarcamalarEntity)

    @Delete
    suspend fun deleteExpence(harcama : HarcamalarEntity)
}