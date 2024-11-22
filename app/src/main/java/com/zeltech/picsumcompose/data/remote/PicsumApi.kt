package com.zeltech.picsumcompose.data.remote

import com.zeltech.picsumcompose.domain.model.Photo
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {

    @GET("list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_LIMIT
    ): List<Photo>

    companion object {
        const val BASE_URL = "https://picsum.photos/v2/"
        const val DEFAULT_LIMIT = 20
    }
}
