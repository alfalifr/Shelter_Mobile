package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AuthApi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toLoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSignupData
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toUpdateReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.console.prine
import sidev.lib.text.getQuoted

class UserRemoteSourceImpl(private val api: AuthApi): UserRemoteSource {
    override suspend fun searchUser(authData: AuthData): Result<User> {
        val body = authData.toLoginBody()
        val call = api.login(body)
        return try {
            val resp = call.execute()
            if(resp.isSuccessful) {
                val res = resp.body()!!
                Success(res.toModel(), 0)
            } else {
                Util.unknownFailResult()
            }
        } catch (e: Throwable) {
            Fail("Email or password don't match", Const.CODE_USER_NOT_FOUND, null)
        }
    }

    override suspend fun registerUser(user: User, authData: AuthData): Result<Boolean> {
        loge("RemoteUserSrc.registerUser() user= $user authData= $")
        prine("RemoteUserSrc.registerUser() user= $user authData= $")
        val body = user.toSignupData(authData.password)
        val call = api.signup(body)
        val resp = call.execute()
        return if(resp.isSuccessful) {
            val res = resp.body()!!
            val code = res.response_code
            if(code == Const.CODE_REGIS_SUCCESS) Success(true, 0)
            else Fail("Email already registered", Const.CODE_REGIS_FAIL, null)
        } else {
            Util.unknownFailResult()
        }
    }

    override suspend fun getUser(email: String): Result<User> = Util.operationNotAvailableFailResult()
    override suspend fun changePassword(
        email: String,
        oldPswd: String,
        newPswd: String
    ): Result<Boolean> = Util.operationNotAvailableFailResult()

    override suspend fun updateUser(oldEmail: String, newData: User, newPswd: String): Result<Boolean> {
        val req = newData.toUpdateReqBody(newPswd)
        val res = api.updateProfile(req).execute()
        if(!res.isSuccessful)
            return Fail("Can't update profile, something not right", res.code(), null)

        val resData = res.body()!!

        return if(resData.response.equals(Const.RESP_OK, true)) Success(true, 0)
        else Fail("Can't update profile", res.code(), null)
    } //Util.operationNotAvailableFailResult()
}