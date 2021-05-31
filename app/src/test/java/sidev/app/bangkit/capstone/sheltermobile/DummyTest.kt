package sidev.app.bangkit.capstone.sheltermobile

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LoginDataResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LoginResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

object DummyTest {
    const val MSG_SUCCESS = "success"
    const val MSG_FAILED = "failed"


    //Login
    //{"response":"success","data":{"id":"2","email":"test111@mail.com","full_name":"test sata","gender":"M"}}
    //{"response":"failed","response_code":101}
    val userIndex = 0
    val loginBody = Dummy.getLoginBody(userIndex)
    val userData = Dummy.userList[userIndex]

    val loginDataResponse_success = userData.let {
        LoginDataResponse(1, it.email, it.name, it.gender)
    }
    val loginResponse_success = LoginResponse(MSG_SUCCESS, loginDataResponse_success)
    val loginResponse_fail = GeneralResponse(MSG_FAILED, Const.CODE_USER_NOT_FOUND)


    //Signup
    //{"response":"success","response_code":104}
    //{"response":"failed","response_code":103}
    val signupBody = Dummy.getSignupBody(userIndex)
    val signupResponse_success = GeneralResponse(MSG_SUCCESS, Const.CODE_REGIS_SUCCESS)
    val signupResponse_fail = GeneralResponse(MSG_FAILED, Const.CODE_REGIS_FAIL)
}