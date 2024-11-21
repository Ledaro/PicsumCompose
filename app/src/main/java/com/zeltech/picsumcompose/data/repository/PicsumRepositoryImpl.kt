package com.zeltech.picsumcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zeltech.picsumcompose.data.paging.PagingSource
import com.zeltech.picsumcompose.data.remote.PicsumApi
import com.zeltech.picsumcompose.domain.model.Photo
import com.zeltech.picsumcompose.domain.repository.PicsumRepository
import kotlinx.coroutines.flow.Flow

class PicsumRepositoryImpl(
    private val picsumApi: PicsumApi
) : PicsumRepository {
    override suspend fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PicsumApi.DEFAULT_LIMIT,
            ),
            pagingSourceFactory = {
                PagingSource(picsumApi)
            }
        ).flow
    }
}
