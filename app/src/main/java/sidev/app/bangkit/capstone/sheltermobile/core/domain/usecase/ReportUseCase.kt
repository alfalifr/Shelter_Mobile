package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import android.graphics.Bitmap
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

interface ReportUseCase: ReportRepo {
    suspend fun getCurrentLocation(): Result<Location>
    suspend fun sendReport(data: Report, img: Bitmap? = null): Result<Boolean>
    //suspend fun sendImg(data: Bitmap): Result<String>
}