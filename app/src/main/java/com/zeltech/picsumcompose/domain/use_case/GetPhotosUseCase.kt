package com.zeltech.picsumcompose.domain.use_case

import com.zeltech.picsumcompose.domain.repository.PicsumRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val picsumRepository: PicsumRepository
) {
    suspend operator fun invoke() = picsumRepository.getPhotos()
}
