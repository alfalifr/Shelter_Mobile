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

    override suspend fun login(authData: AuthData): Result<Boolean> {
        return when(val res = userRemoteSrc.searchUser(authData).also { loge("UserUseCaseImpl login() authData= $authData res = $it") }){
            is Success -> {
                val user = res.data

                val isPswdResSuccess = userLocalSrc.savePassword(authData.password) is Success
                val isEmailResSuccess = userLocalSrc.saveEmail(authData.email) is Success
                val isUserResSuccess = userLocalSrc.saveUser(user) is Success
                //val isSetDefaultLocSuccess = setDefaultCurrentLocation() is Success
                val getLocRes = locationLocalSrc.getLocationByName(user.location.name)
                if(getLocRes !is Success)
                    return getLocRes as Fail

                val newLoc = getLocRes.data
                val isSaveLocSuccess = saveCurrentLocation(newLoc) is Success

                loge("isPswdResSuccess= $isPswdResSuccess isEmailResSuccess= $isEmailResSuccess isUserResSuccess= $isUserResSuccess isSaveLocSuccess= $isSaveLocSuccess")

                when {
                    isPswdResSuccess && isEmailResSuccess && isSaveLocSuccess && isUserResSuccess -> Success(true, 0)
                    !isPswdResSuccess && !isEmailResSuccess && !isSaveLocSuccess && !isUserResSuccess -> Fail("Can't save both email, password, and default location to local", -1, null)
                    else -> Success(true, 1)
                }
            }
            is Fail -> res
        }
    }

    override suspend fun signup(user: User, authData: AuthData): Result<Boolean> = when(val remoteRes = userRemoteSrc.registerUser(user, authData)){
        is Success -> when(val localRes = userLocalSrc.saveUser(user)){
            is Success -> {
                val isPswdResSuccess = userLocalSrc.savePassword(authData.password) is Success
                val isEmailResSuccess = userLocalSrc.saveEmail(authData.email) is Success
                val isUserResSuccess = userLocalSrc.saveUser(user) is Success
                //val isSetDefaultLocSuccess = setDefaultCurrentLocation() is Success
                val isSaveLocSuccess = saveCurrentLocation(user.location) is Success

                userRemoteSrc.updateUser(user.email, user, authData.password) //TODO optimasi, dari server.

                when {
                    isPswdResSuccess && isEmailResSuccess && isSaveLocSuccess && isUserResSuccess-> Success(true, 0)
                    !isPswdResSuccess && !isEmailResSuccess && !isSaveLocSuccess && !isUserResSuccess -> Fail("Can't save both email, password, and default location to local", -1, null)
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

    override suspend fun saveCurrentProfile(user: User, password: String?): Result<Boolean> {
        val userRes = userLocalSrc.getCurrentUser()
        if(userRes !is Success)
            return userRes as Fail

        val oldUser = userRes.data

        if(password != null) {
            val pswdRes = userLocalSrc.getPassword()
            if(pswdRes !is Success)
                return pswdRes as Fail

            val oldPswd = pswdRes.data

            when(val res = userRemoteSrc.changePassword(oldUser.email, oldPswd, password)) {
                is Fail -> return res
            }
        }

        val pswd = password ?: userLocalSrc.getPassword().let {
            if(it !is Success) return Fail("Can't get old password from local", -1, null)
            it.data
        }

        return when(val remRes = userRemoteSrc.updateUser(oldUser.email, user, pswd)) {
            is Success -> userLocalSrc.updateUser(oldUser.email, user, pswd)
            is Fail -> remRes
        }
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