package com.emreozcan.harcamalaruygulamasi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar

/**
 * Databases and RecyclerView dersinden örnek alınarak oluşturulmuştur
 */
@Database(entities = [Harcamalar::class],version = 2)
abstract class Veritabani : RoomDatabase() {
    abstract val harcamalarDao : HarcamalarDao

    companion object{
        @Volatile
        private var ORNEK_NESNE : Veritabani? = null
        fun ornegiGetir(baglam : Context): Veritabani {
            synchronized(this){
                var ornek = ORNEK_NESNE;
                if(ornek== null){
                    ornek = Room.databaseBuilder(
                        baglam.applicationContext, Veritabani::class.java,
                        "harcamalar"
                    )
                        .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                        ORNEK_NESNE = ornek;
                }
                return  ornek;
            }
        }
    }
}