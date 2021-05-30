package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

interface UserRemoteSource: UserRepo {
    suspend fun searchUser(authData: AuthData): Result<User>
    suspend fun registerUser(user: User, authData: AuthData): Result<Boolean>
    suspend fun changePassword(email: String, oldPswd: String, newPswd: String): Result<Boolean>
}