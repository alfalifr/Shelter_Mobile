package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.ReportApi
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toReportReqBody
import sidev.lib.console.prin

class ReportApiTest {
    companion object {
        val api: ReportApi by lazy { AppRetrofit.reportApi }
    }

    @Test
    fun sendReportTest(){
        val user = Dummy.userList[0]
        val req = Dummy.formList[0].toReportReqBody(user.email)

        val res = api.sendReport(req).execute()

        prin("res = $res")

        assert(res.isSuccessful)

        val data = res.body()!!
        prin("data = $data")

        assertEquals(Const.RESP_OK, data.response)
    }
}