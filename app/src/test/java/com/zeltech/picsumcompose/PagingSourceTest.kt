package com.zeltech.picsumcompose

import androidx.paging.PagingSource
import com.zeltech.picsumcompose.data.paging.PhotoPagingSource
import com.zeltech.picsumcompose.data.remote.PicsumApi
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PhotoPagingSourceTest {
    private val mockApi: PicsumApi = mockk()

    @Test
    fun `refresh load should return expected PageData`() = runTest {
        val mockPhotos = listOf(MockPhotoFactory.createPhoto())
        coEvery { mockApi.getImages(page = 1, limit = 20) } returns mockPhotos

        val photoPagingSource = PhotoPagingSource(mockApi)
        val result = photoPagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        with(result as PagingSource.LoadResult.Page) {
            assertEquals(mockPhotos, data)
            assertNull(prevKey)
            assertEquals(2, nextKey)
        }
    }

    @Test
    fun `refresh load should return IOException error`() = runTest {
        coEvery { mockApi.getImages(page = 1, limit = 20) } throws IOException("Network error")

        val pagingSource = PhotoPagingSource(mockApi)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertTrue(error.throwable is IOException)
    }

    @Test
    fun `refresh load should return HttpException error`() = runTest {
        val exception = HttpException(
            Response.error<Any>(
                404,
                "Not Found".toResponseBody(null)
            )
        )
        coEvery { mockApi.getImages(page = 1, limit = 20) } throws exception

        val pagingSource = PhotoPagingSource(mockApi)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 20, false))

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(exception, error.throwable)
    }

    @Test
    fun `refresh load should return correct keys for pagination`() = runTest {
        val photos = listOf(MockPhotoFactory.createPhoto())
        coEvery { mockApi.getImages(page = 2, limit = 20) } returns photos

        val pagingSource = PhotoPagingSource(mockApi)
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(2, 20, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(photos, page.data)
        assertEquals(1, page.prevKey)
        assertEquals(3, page.nextKey)
    }

    @Test
    fun `append load should return correct keys and data`() = runTest {
        val photos = listOf(MockPhotoFactory.createPhoto())
        coEvery { mockApi.getImages(page = 2, limit = 20) } returns photos

        val pagingSource = PhotoPagingSource(mockApi)
        val result = pagingSource.load(PagingSource.LoadParams.Append(2, 20, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(photos, page.data)
        assertEquals(1, page.prevKey)
        assertEquals(3, page.nextKey)
    }

    @Test
    fun `prepend load should return correct keys and data`() = runTest {
        val photos = listOf(MockPhotoFactory.createPhoto())
        coEvery { mockApi.getImages(page = 2, limit = 20) } returns photos

        val pagingSource = PhotoPagingSource(mockApi)
        val result = pagingSource.load(PagingSource.LoadParams.Prepend(2, 20, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(photos, page.data)
        assertEquals(1, page.prevKey)
        assertEquals(3, page.nextKey)
    }
}
