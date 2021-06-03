package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.ReportLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.ReportRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success

class ReportUseCaseImpl(
    private val repo: ReportRepo,
    private val localSrc: ReportLocalSource,
    private val remoteSrc: ReportRemoteSource,
    private val locationLocalSrc: LocationLocalSource,
    private val userLocalSrc: UserLocalSource,
): ReportUseCase, ReportRepo by repo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
    override suspend fun sendReport(data: Report): Result<Boolean> =
        when(val userRes = userLocalSrc.getCurrentUser()) {
            is Success -> {
                when(val remRes = remoteSrc.sendReport(data, userRes.data)){
                    is Success -> {
                        val reportDetail = ReportDetail(data, "")
                        when(val locRes = localSrc.saveReportDetail(reportDetail)) {
                            is Success -> Success(true, 0)
                            is Fail -> locRes
                        }
                    }
                    is Fail -> remRes
                }
            }
            is Fail -> userRes
        }
}