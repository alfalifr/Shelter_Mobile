package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

interface UserLocalSource: UserRepo {
    suspend fun getCurrentUser(): Result<User>
    suspend fun saveUser(data: User): Result<Boolean>
    suspend fun saveEmail(email: String): Result<Boolean>
    suspend fun savePassword(password: String): Result<Boolean>
    suspend fun getPassword(): Result<String>
}