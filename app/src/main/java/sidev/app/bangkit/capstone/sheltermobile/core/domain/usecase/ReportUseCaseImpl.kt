package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

class ReportUseCaseImpl(
    private val repo: ReportRepo,
    private val locationLocalSrc: LocationLocalSource,
): ReportUseCase, ReportRepo by repo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
}