package com.farmassist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmassist.data.local.model.NewsEntity
import com.farmassist.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _newsState = MutableStateFlow<List<NewsEntity>>(emptyList())
    val newsState: StateFlow<List<NewsEntity>> = _newsState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Collect local cache
        viewModelScope.launch {
            repository.getNews().collect { localNews ->
                _newsState.value = localNews
            }
        }
        
        // Fetch new network data
        refreshNews()
    }

    fun refreshNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.refreshNews()
            } catch (e: Exception) {
                // We don't clear the local cache on error, so users still see offline news
                _errorMessage.value = "Unable to fetch live news. Showing offline cache."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
