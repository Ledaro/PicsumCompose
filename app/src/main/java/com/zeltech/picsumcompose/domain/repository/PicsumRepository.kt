package com.zeltech.picsumcompose.domain.repository

import androidx.paging.PagingData
import com.zeltech.picsumcompose.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PicsumRepository {
    suspend fun getPhotos(): Flow<PagingData<Photo>>
}
