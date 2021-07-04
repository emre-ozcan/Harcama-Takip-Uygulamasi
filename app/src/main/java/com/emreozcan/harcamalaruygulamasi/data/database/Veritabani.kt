package com.emreozcan.harcamalaruygulamasi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity


@Database(entities = [HarcamalarEntity::class],version = 1,exportSchema = false)
@TypeConverters(KategoriTypeConverter::class)
abstract class Veritabani : RoomDatabase() {

    abstract fun harcamalarDao(): HarcamalarDao

}