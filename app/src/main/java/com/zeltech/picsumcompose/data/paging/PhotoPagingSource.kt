package com.zeltech.picsumcompose.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.zeltech.picsumcompose.data.remote.PicsumApi
import com.zeltech.picsumcompose.domain.model.Photo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PhotoPagingSource @Inject constructor(
    private val api: PicsumApi
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val currentPage = params.key ?: 1
            val photos = api.getPhotos(page = currentPage, limit = PicsumApi.DEFAULT_LIMIT)

            LoadResult.Page(
                data = photos,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (photos.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }
}
