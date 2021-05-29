package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo

class LocationUseCaseImpl(
    private val repo: LocationRepo,
): LocationUseCase, LocationRepo by repo