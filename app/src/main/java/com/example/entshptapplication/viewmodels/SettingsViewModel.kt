package com.example.entshptapplication.viewmodels

import androidx.lifecycle.*
import com.example.entshptapplication.models.ConnectionSetting
import com.example.entshptapplication.repository.SettingsDbRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsDbRepository): ViewModel( ) {
    val setting:LiveData<ConnectionSetting> = repository.setting.asLiveData()
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