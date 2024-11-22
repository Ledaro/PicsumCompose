package com.zeltech.picsumcompose.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.zeltech.picsumcompose.domain.model.Photo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val PhotoDetails = object : NavType<Photo>(
        isNullableAllowed = false,
    ) {
        override fun get(
            bundle: Bundle,
            key: String
        ): Photo? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Photo {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Photo): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: Photo
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
