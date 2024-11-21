package com.zeltech.picsumcompose.domain.use_case

import com.zeltech.picsumcompose.domain.repository.PicsumRepository

class GetPhotosUseCase(
    private val picsumRepository: PicsumRepository
) {
    suspend operator fun invoke() = picsumRepository.getPhotos()
}
