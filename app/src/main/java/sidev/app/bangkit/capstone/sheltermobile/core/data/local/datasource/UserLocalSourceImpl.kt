package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.UserDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class UserLocalSourceImpl(private val dao: UserDao, private val ctx: Context): UserLocalSource {
    override suspend fun getUser(email: String): Result<User> {
        val data = dao.getUser(email)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun saveUser(data: User): Result<Boolean> {
        val entity = data.toEntity()
        val insertedCount = dao.saveUser(entity)
        return if(insertedCount == 1) Success(true, 0)
        else Util.cantInsertFailResult()
    }

    override suspend fun getCurrentUser(): Result<User> {
        val email = Util.getSharedPref(ctx).getString(Const.KEY_USER_EMAIL, null)
            ?: return Util.noValueFailResult()
        return getUser(email)
    }

    override suspend fun savePassword(password: String): Result<Boolean> {
        Util.editSharedPref(ctx, true) {
            putString(Const.KEY_PASSWORD, password)
        }
        return Success(true, 0)
    }

    override suspend fun getPassword(): Result<String> {
        val pswd = Util.getSharedPref(ctx).getString(Const.KEY_PASSWORD, null) ?: return Util.noValueFailResult()
        return Success(pswd, 0)
    }
}