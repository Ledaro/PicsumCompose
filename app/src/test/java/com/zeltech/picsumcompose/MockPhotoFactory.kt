package com.zeltech.picsumcompose

import com.zeltech.picsumcompose.domain.model.Photo

object MockPhotoFactory {
    fun createPhoto(
        id: Int = 1,
        author: String = "Author",
        width: Int = 100,
        height: Int = 200,
        url: String = "url",
        downloadUrl: String = "download_url"
    ): Photo {
        return Photo(id, author, width, height, url, downloadUrl)
    }
}
