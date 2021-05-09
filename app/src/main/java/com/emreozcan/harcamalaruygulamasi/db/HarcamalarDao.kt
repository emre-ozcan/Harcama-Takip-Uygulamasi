package com.emreozcan.harcamalaruygulamasi.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar

@Dao
interface HarcamalarDao {

    //Bu bizim veritabanı ile iletişim interfacesimizdir. Sorgular yapmamıza olanak tanır ve bu cevapları bize döndürür

    @Query("SELECT * FROM harcamalar")
    fun getAll(): List<Harcamalar>

    @Insert
    fun insertAll(vararg harcama: Harcamalar)

    @Delete
    fun delete(harcama : Harcamalar)
}