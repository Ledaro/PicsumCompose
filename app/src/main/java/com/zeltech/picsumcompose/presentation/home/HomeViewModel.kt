package com.zeltech.picsumcompose.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zeltech.picsumcompose.domain.model.Photo
import com.zeltech.picsumcompose.domain.use_case.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photos = _photos

    init {
        fetchImages()
    }

    private fun fetchImages() = viewModelScope.launch {
        getPhotosUseCase()
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect {
                _photos.value = it
            }
    }
}
