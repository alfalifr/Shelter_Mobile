package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

class UserUseCaseImpl(
    private val repo: UserRepo,
    private val userLocalSrc: UserLocalSource,
    private val userRemoteSrc: UserRemoteSource,
    private val locationLocalSrc: LocationLocalSource,
): UserUseCase, UserRepo by repo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
    override suspend fun getCurrentUser(): Result<User> = userLocalSrc.getCurrentUser()

    override suspend fun login(authData: AuthData): Result<Boolean> = when(val res = userRemoteSrc.searchUser(authData)){
        is Success -> {
            val isPswdResSuccess = userLocalSrc.savePassword(authData.password) is Success
            val isEmailResSuccess = userLocalSrc.saveEmail(authData.email) is Success

            when {
                isPswdResSuccess && isEmailResSuccess -> Success(true, 0)
                !isPswdResSuccess && !isEmailResSuccess -> Fail("Can't save both email and password to local", -1, null)
                else -> Success(true, 1)
            }
        }
        is Fail -> res
    }

    override suspend fun signup(user: User, authData: AuthData): Result<Boolean> = when(val remoteRes = userRemoteSrc.registerUser(user, authData)){
        is Success -> when(val localRes = userLocalSrc.saveUser(user)){
            is Success -> {
                val isPswdResSuccess = userLocalSrc.savePassword(authData.password) is Success
                val isEmailResSuccess = userLocalSrc.saveEmail(authData.email) is Success

                when {
                    isPswdResSuccess && isEmailResSuccess -> Success(true, 0)
                    !isPswdResSuccess && !isEmailResSuccess -> Fail("Can't save both email and password to local", -1, null)
                    else -> Success(true, 1)
                }
            }
            is Fail -> localRes
        }
        is Fail -> remoteRes
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Boolean> = TODO()
//= when(val remoteRes = userRemoteSrc.savePassword())
}