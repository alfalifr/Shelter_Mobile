package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.UserLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.UserRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import java.lang.IllegalArgumentException

class UserCompositeSource(
    private val localSrc: UserLocalSource,
    private val remoteSrc: UserRemoteSource,
): CompositeDataSource<User>(), UserRepo, UserLocalSource {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<User>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<User>> = getDataList(remoteSrc, args)


    private suspend fun getDataList(repo: UserRepo, args: Map<String, Any?>): Result<List<User>> {
        val email = args[Const.KEY_EMAIL] as? String ?: throw IllegalArgumentException("args[Const.KEY_EMAIL] == null")
        return repo.getUser(email).toListResult()
    }

    override suspend fun saveDataList(remoteDataList: List<User>): Result<Int> = when(val res = localSrc.saveUser(remoteDataList.first())){
        is Success -> Success(1, 0)
        is Fail -> res
    }


    override suspend fun getUser(email: String): Result<User> = getDataList(mapOf(
        Const.KEY_EMAIL to email,
    )).toSingleResult()

    override suspend fun saveUser(data: User): Result<Boolean> = when(val localRes = localSrc.saveUser(data)){
        is Success -> Success(true, 0)
        is Fail -> localRes
    }

    override suspend fun savePassword(password: String): Result<Boolean> = when(val localRes = localSrc.savePassword(password)){
        is Success -> Success(true, 0)
        is Fail -> localRes
    }

    override suspend fun getPassword(): Result<String> = localSrc.getPassword()

    override suspend fun getCurrentUser(): Result<User> = localSrc.getCurrentUser()

    override suspend fun deleteCurrentUser(): Result<Boolean> = localSrc.deleteCurrentUser()

    override suspend fun saveEmail(email: String): Result<Boolean> = localSrc.saveEmail(email)
}