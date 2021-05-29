package sidev.app.bangkit.capstone.sheltermobile.core.domain.usecase

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

class UserUseCaseImpl(
    private val repo: UserRepo,
    private val userLocalSrc: UserLocalSource,
    private val locationLocalSrc: LocationLocalSource,
): UserUseCase, UserRepo by repo {
    override suspend fun getCurrentLocation(): Result<Location> = locationLocalSrc.getCurrentLocation()
    override suspend fun getCurrentUser(): Result<User> = userLocalSrc.getCurrentUser()

}