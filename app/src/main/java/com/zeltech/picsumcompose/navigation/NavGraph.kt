package com.zeltech.picsumcompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.zeltech.picsumcompose.domain.model.Photo
import com.zeltech.picsumcompose.presentation.details.DetailScreen
import com.zeltech.picsumcompose.presentation.home.HomeScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data object HomeRoute

@Serializable
data class DetailRoute(val photo: Photo)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SetUpNavGraph(navController: NavHostController) {

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    animatedVisibilityScope = this,
                    onPhotoClick = { photo ->
                        navController.navigate(DetailRoute(photo))
                    }
                )
            }

            composable<DetailRoute>(
                typeMap = mapOf(
                    typeOf<Photo>() to CustomNavType.PhotoDetails
                )
            ) {
                val arguments = it.toRoute<DetailRoute>()
                DetailScreen(
                    animatedVisibilityScope = this,
                    photo = arguments.photo
                )
            }
        }
    }
}
