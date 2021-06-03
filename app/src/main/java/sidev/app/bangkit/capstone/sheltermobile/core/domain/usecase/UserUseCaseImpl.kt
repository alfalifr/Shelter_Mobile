package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.lib.android.std.tool.util.`fun`.loge

class UserUseCaseImpl(
    private val repo: UserRepo,
    private val userLocalSrc: UserLocalSource,
    private val userRemoteSrc: UserRemoteSource,
    private val locationLocalSrc: LocationLocalSource,
    private val locationRepo: LocationRepo,
): UserUseCase, UserRepo by repo, LocationRepo by locationRepo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
    override suspend fun getCurrentUser(): Result<User> = userLocalSrc.getCurrentUser()

    override suspend fun login(authData: AuthData): Result<Boolean> = when(val res = userRemoteSrc.searchUser(authData).also { loge("UserUseCaseImpl login() authData= $authData res = $it") }){
        is Success -> {
            val isPswdResSuccess = userLocalSrc.savePassword(authData.password) is Success
            val isEmailResSuccess = userLocalSrc.saveEmail(authData.email) is Success
            val isUserResSuccess = userLocalSrc.saveUser(res.data) is Success
            val isSetDefaultLocSuccess = setDefaultCurrentLocation() is Success

            loge("isPswdResSuccess= $isPswdResSuccess isEmailResSuccess= $isEmailResSuccess isUserResSuccess= $isUserResSuccess isSetDefaultLocSuccess= $isSetDefaultLocSuccess")

            when {
                isPswdResSuccess && isEmailResSuccess && isSetDefaultLocSuccess && isUserResSuccess -> Success(true, 0)
                !isPswdResSuccess && !isEmailResSuccess && !isSetDefaultLocSuccess && !isUserResSuccess -> Fail("Can't save both email, password, and default location to local", -1, null)
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
                val isUserResSuccess = userLocalSrc.saveUser(user) is Success
                val isSetDefaultLocSuccess = setDefaultCurrentLocation() is Success

                when {
                    isPswdResSuccess && isEmailResSuccess && isSetDefaultLocSuccess && isUserResSuccess-> Success(true, 0)
                    !isPswdResSuccess && !isEmailResSuccess && !isSetDefaultLocSuccess && !isUserResSuccess -> Fail("Can't save both email, password, and default location to local", -1, null)
                    else -> Success(true, 1)
                }
            }
            is Fail -> localRes
        }
        is Fail -> remoteRes
    }

    override suspend fun logout(): Result<Boolean> = userLocalSrc.deleteCurrentUser()

    override suspend fun saveCurrentLocation(data: Location): Result<Boolean> = locationLocalSrc.saveCurrentLocation(data)
    override suspend fun setDefaultCurrentLocation(): Result<Boolean> = when(val locRes = locationRepo.getAllLocation()) {
        is Success -> {
            val firstData = locRes.data.first()
            saveCurrentLocation(firstData)
        }
        is Fail -> locRes
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Boolean> =
        when(val localRes = userLocalSrc.getCurrentUser()){
            is Success -> {
                val email = localRes.data.email
                when(val remoteRes = userRemoteSrc.changePassword(email, oldPassword, newPassword)){
                    is Success -> {
                        when(val localRes2 = userLocalSrc.savePassword(newPassword)){
                            is Success -> Success(true, 0)
                            is Fail -> localRes2
                        }
                    }
                    is Fail -> remoteRes
                }
            }
            is Fail -> localRes
        }

    override suspend fun getPassword(): Result<String> = userLocalSrc.getPassword()

    //= when(val remoteRes = userRemoteSrc.savePassword())
}