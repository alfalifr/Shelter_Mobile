package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import android.graphics.Bitmap
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.ReportRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

interface ReportRemoteSource: ReportRepo {
    suspend fun sendReport(data: Report, user: User): Result<Boolean>
    suspend fun sendImg(data: Bitmap): Result<String>
}