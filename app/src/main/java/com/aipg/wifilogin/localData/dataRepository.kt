package com.aipg.wifilogin.localData

import androidx.lifecycle.LiveData

class dataRepository(private val dataDao: dataDao) {
    val allData: LiveData<List<dataClass>> = dataDao.getAllDataLive()

    suspend fun insert(dataClasse: dataClass) {
        dataDao.insert(dataClasse)
    }

    suspend fun delete(dataClasse: dataClass) {
        dataDao.delete(dataClasse)
    }
}