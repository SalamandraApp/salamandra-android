package com.android.salamandra.data.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenManager @Inject constructor(
    private val dataStore: DataStoreRepository,
): ViewModel() {

    private val token = MutableLiveData<String?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.getToken().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }
        }
    }

    fun getToken() = token.value

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveToken(token)
        }
    }

    fun deleteToken() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.deleteToken()
        }
    }
}
