package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

interface UserUseCase : UserRepo, LocationRepo {
    suspend fun login(authData: AuthData): Result<Boolean>
    suspend fun signup(user: User, authData: AuthData): Result<Boolean>
    suspend fun logout(): Result<Boolean>
    suspend fun getCurrentLocation(): Result<Location>
    suspend fun getCurrentUser(): Result<User>
    suspend fun getPassword(): Result<String>
    suspend fun saveCurrentLocation(data: Location): Result<Boolean>
    suspend fun setDefaultCurrentLocation(): Result<Boolean>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Boolean>
}