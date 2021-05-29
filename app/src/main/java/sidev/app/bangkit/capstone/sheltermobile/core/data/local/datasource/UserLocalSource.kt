package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.UserRepo

interface UserLocalSource: UserRepo {
    suspend fun getCurrentUser(): Result<User>
}