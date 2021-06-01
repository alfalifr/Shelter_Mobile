package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AuthApi
import sidev.lib.console.prine

class AuthApiTest {
    companion object {
        val api: AuthApi by lazy { AppRetrofit.authApi }

    }

    @Test
    fun _1_signup() {
        //api.
        val body = DummyTest.signupBody
        val call = api.signup(body)
        val resp = call.execute()

        assert(resp.isSuccessful)

        val respData = resp.body()

        prine("SIGNUP respData = $respData")

        assertNotNull(respData)
        assertEquals(DummyTest.signupResponse_success, respData)
    }

    @Test
    fun _2_login() {
        //api.login(Dummy.getLoginBody())
        val body = DummyTest.loginBody
        val call = api.login(body)
        val resp = call.execute()

        assert(resp.isSuccessful)

        val respData = resp.body()

        prine("LOGIN respData = $respData")

        assertNotNull(respData)
        assertEquals(DummyTest.loginResponse_success, respData)
    }
}