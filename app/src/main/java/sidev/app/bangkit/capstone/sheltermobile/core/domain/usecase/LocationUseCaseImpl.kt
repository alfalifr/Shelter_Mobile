package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

class LocationUseCaseImpl(
    private val repo: LocationRepo,
    private val localSrc: LocationLocalSource,
): LocationUseCase, LocationRepo by repo {
    override suspend fun saveCurrentLocation(data: Location): Result<Boolean> = localSrc.saveCurrentLocation(data)
}