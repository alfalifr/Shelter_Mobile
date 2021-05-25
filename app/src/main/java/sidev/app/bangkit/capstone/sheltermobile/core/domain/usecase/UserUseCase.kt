package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

interface UserUseCase : UserRepo {
    override suspend fun getUser(email: String): Result<User>
    override suspend fun saveUser(data: User): Result<Boolean>
    suspend fun login(authData: AuthData): Result<Boolean>
    suspend fun signup(data: User, authData: AuthData): Result<Boolean>
}