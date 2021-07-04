package com.emreozcan.harcamalaruygulamasi.data

import com.emreozcan.harcamalaruygulamasi.data.database.HarcamalarDao
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val harcamalarDao: HarcamalarDao
) {

    fun readExpences(): Flow<List<HarcamalarEntity>> {
        return harcamalarDao.getAllExpences()
    }

    suspend fun insertExpence(harcamalarEntity: HarcamalarEntity){
        harcamalarDao.insertExpence(harcamalarEntity)
    }

    suspend fun deleteExpence(harcamalarEntity: HarcamalarEntity){
        harcamalarDao.deleteExpence(harcamalarEntity)
    }

}