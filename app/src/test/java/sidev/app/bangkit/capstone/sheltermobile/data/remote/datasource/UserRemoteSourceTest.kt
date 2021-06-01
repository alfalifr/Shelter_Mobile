package sidev.app.bangkit.capstone.sheltermobile.data.remote.datasource

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSourceImpl
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.lib.console.prin

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserRemoteSourceTest {
    companion object {
        val src: UserRemoteSource by lazy { UserRemoteSourceImpl(AppRetrofit.authApi) }
    }

    @Test
    fun _1_signup() {
        runBlocking {
            val res = src.registerUser(DummyTest.userData, DummyTest.userAuthData)
            prin(res)

            assert(res is Success)

            val data = (res as Success).data
            assert(data)
        }
    }

    @Test
    fun _2_login() {
        runBlocking {
            val res = src.searchUser(DummyTest.userAuthData)
            prin(res)

            assert(res is Success)

            val data = (res as Success).data
            assertEquals(DummyTest.userData, data)
        }
    }
}