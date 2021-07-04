package com.emreozcan.harcamalaruygulamasi.di

import android.content.Context
import androidx.room.Room
import com.emreozcan.harcamalaruygulamasi.data.database.Veritabani
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.EXPENSE_TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, Veritabani::class.java, EXPENSE_TABLE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: Veritabani) = database.harcamalarDao()

}