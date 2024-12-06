package com.aipg.wifilogin.localData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class dataViewModel(application: Application) : AndroidViewModel(application) {
    val allData: LiveData<List<dataClass>>
    val repository: dataRepository

    init {
        val dao = DataBases.getDataBase(application).getDataDao()
        repository = dataRepository(dao)
        allData = repository.allData
    }
    fun insertStock(dataClasse: dataClass) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(dataClasse)
    }
    fun deleteStock(dataClasse: dataClass) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(dataClasse)
    }
}