package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import android.graphics.Bitmap
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.ReportLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.ReportRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.android.std.tool.util.`fun`.loge

class ReportUseCaseImpl(
    private val repo: ReportRepo,
    private val localSrc: ReportLocalSource,
    private val remoteSrc: ReportRemoteSource,
    private val locationLocalSrc: LocationLocalSource,
    private val userLocalSrc: UserLocalSource,
): ReportUseCase, ReportRepo by repo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
    override suspend fun sendReport(data: Report, img: Bitmap?): Result<Boolean> =
        when(val userRes = userLocalSrc.getCurrentUser()) {
            is Success -> {
                loge("sendReport img = $img")
                val imgLink = if(img != null){
                    val res = remoteSrc.sendImg(img)
                    loge("remoteSrc.sendImg() res = $res")
                    if(res is Success) res.data.also {
                        val isHttp = "http" in it
                        val isStar = "*" in it
                        loge("remoteSrc.sendImg() imgLink = $it isHttp= $isHttp isStar= $isStar")
                    }
                    else ""
                } else ""

                val form = data.form
                val sentReport = if(form != null) {
                    val sentForm = form.copy(photoLinkList = listOf(imgLink))
                    data.copy(form = sentForm)
                } else data

                //val sentData = data.copy(form = form)

                when(val remRes = remoteSrc.sendReport(sentReport, userRes.data)){
                    is Success -> {
                        val reportDetail = ReportDetail(sentReport, "")
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

    //override suspend fun sendImg(data: Bitmap): Result<String> = remoteSrc.sendImg(data)
}