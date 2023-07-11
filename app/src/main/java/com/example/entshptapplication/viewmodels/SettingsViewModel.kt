package com.example.entshptapplication.viewmodels

import androidx.lifecycle.*
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.repository.SettingsDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(private val repository: SettingsDbRepository): ViewModel( ) {
    suspend fun getSetting() = withContext(Dispatchers.IO){
        return@withContext repository.getSetting()
    }
    fun insert(connectionSetting: ConnectionSetting) = viewModelScope.launch {
        repository.insert(connectionSetting)
    }
    fun clear() = viewModelScope.launch {
        repository.clear()
    }
}

class SettingsViewModelFactory(private val repository: SettingsDbRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}