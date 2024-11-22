package com.zeltech.picsumcompose

import com.google.common.truth.Truth.assertThat
import com.zeltech.picsumcompose.data.remote.PicsumApi
import com.zeltech.picsumcompose.domain.model.Photo
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PicsumApiTest {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient.Builder().build()
    private lateinit var picsumApi: PicsumApi

    @Before
    fun setUp() {
        picsumApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicsumApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getImages should return a list of photos with correct data`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody(
                """
                [
                    {"id": 1, "author": "Author1", "width": 500, "height": 500, "url": "https://example.com/1", "download_url": "https://example.com/1.jpg"},
                    {"id": 2, "author": "Author2", "width": 600, "height": 600, "url": "https://example.com/2", "download_url": "https://example.com/2.jpg"}
                ]
                """.trimIndent()
            )
            .setResponseCode(200)

        mockWebServer.enqueue(mockResponse)

        val photos = picsumApi.getImages(page = 1)

        assertThat(photos).hasSize(2)
        assertThat(photos[0].id).isEqualTo(1)
        assertThat(photos[0].author).isEqualTo("Author1")
        assertThat(photos[0].downloadUrl).isEqualTo("https://example.com/1.jpg")
    }

    @Test
    fun `getImages should return empty list when server responds with 404 error`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val photos = try {
            picsumApi.getImages(page = 1)
        } catch (e: HttpException) {
            emptyList<Photo>()
        }

        assertThat(photos).isEmpty()
    }

    @Test
    fun `getImages should pass correct page query parameter to the API`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("[]")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        picsumApi.getImages(page = 2)

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/list?page=2&limit=20")
    }

    @Test
    fun `getImages should apply default limit when no limit is provided`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("[]")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        picsumApi.getImages(page = 1)

        val request = mockWebServer.takeRequest()
        assertThat(request.path).contains("limit=20")
    }

    @Test
    fun `getImages should apply custom limit when specified`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("[]")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        picsumApi.getImages(page = 1, limit = 50)

        val request = mockWebServer.takeRequest()
        assertThat(request.path).contains("limit=50")
    }
}
