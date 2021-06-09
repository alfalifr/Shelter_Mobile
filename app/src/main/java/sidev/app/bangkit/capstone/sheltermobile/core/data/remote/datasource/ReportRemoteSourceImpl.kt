package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import android.graphics.Bitmap
import android.util.Base64
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.ImgApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.ReportApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.ImageReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.ReportDetail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toReportReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.compressToJpg
import sidev.lib.android.std.tool.util.`fun`.loge

class ReportRemoteSourceImpl(
    private val api: ReportApi,
    private val imgApi: ImgApi,
): ReportRemoteSource {
    override suspend fun sendReport(data: Report, user: User): Result<Boolean> {
        val form = data.form ?: return Success(true,0)
/*
        val userRes = userLocalSrc.getCurrentUser()
        if(userRes !is Success)
            return userRes as Fail
 */
        val req = form.toReportReqBody(user.email)
        val res = api.sendReport(req).execute()
        if(!res.isSuccessful)
            return Fail("Can't send form", -1, null)

        val resData = res.body()!!
        return if(resData.response == Const.RESP_OK) Success(true, 0)
        else Fail("Can't send form, something wrong", -1, null)
    }

    override suspend fun sendImg(data: Bitmap): Result<String> {
        val jpgBytes = data.compressToJpg()
        val base64Bytes = Base64.encode(jpgBytes, Base64.DEFAULT)
        val req = ImageReqBody(base64Bytes.decodeToString())

        val res = imgApi.uploadImg(req).execute()
        if(!res.isSuccessful)
            return Fail("Can't upload image", res.code(), null)

        val resData = res.body()!!
        return if(resData.status == Const.CODE_OK) {
            val imgLink = resData.data.link!!
            loge("Uploaded img link = $imgLink")
            Success(imgLink, 0)
        } else Fail("Can't upload image", resData.status, null)
    }

    override suspend fun getReportList(top: Int): Result<List<Report>> = Success(emptyList(), 0)

    override suspend fun getReportDetailList(top: Int): Result<List<ReportDetail>> = Success(emptyList(), 0)

    override suspend fun getReportDetail(timestamp: TimeString): Result<ReportDetail> = Util.operationNotAvailableFailResult()

    override suspend fun saveReportDetail(data: ReportDetail): Result<Int> = Util.operationNotAvailableFailResult()
/*
    {
        val form = data.report.form ?: return Success(1,0)
        val userRes = userLocalSrc.getCurrentUser()
        if(userRes !is Success)
            return userRes as Fail

        val req = form.toReportReqBody(userRes.data.email)
        val res = api.sendReport(req).execute()
        if(!res.isSuccessful)
            return Fail("Can't send form", -1, null)

        val resData = res.body()!!
        return if(resData.response == Const.RESP_OK) Success(1, 0)
        else Fail("Can't send form, something wrong", -1, null)
    }
 */

    override suspend fun saveReportDetailList(list: List<ReportDetail>): Result<Int> = Util.operationNotAvailableFailResult()
/*
    {
        var successCount = 0
        for(report in list) {
            val res = saveReportDetail(report)
            if(res is Success)
                successCount++
        }
        return Util.getInsertResult(successCount, list.size)
    }
 */
}