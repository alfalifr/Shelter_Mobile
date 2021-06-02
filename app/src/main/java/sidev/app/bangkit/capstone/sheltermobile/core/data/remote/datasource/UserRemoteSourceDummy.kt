package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.collection.findIndexed

object UserRemoteSourceDummy: UserRemoteSource {
    override suspend fun searchUser(authData: AuthData): Result<User> = Dummy.userList.findIndexed { (i, user) ->
        user.email == authData.email && Dummy.userPswd[i] == authData.password
    }?.let { Success(it.value, 0) } ?: Util.noValueFailResult()

    override suspend fun registerUser(user: User, authData: AuthData): Result<Boolean> = Success(true, 0)

    override suspend fun changePassword(
        email: String,
        oldPswd: String,
        newPswd: String
    ): Result<Boolean> = Success(true, 0)

    override suspend fun getUser(email: String): Result<User> = Dummy.userList.find { it.email == email }
        ?.let { Success(it, 0) } ?: Util.noValueFailResult()
}