package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.UserDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class UserLocalSourceImpl(
    private val dao: UserDao,
    private val localLocationSrc: LocationLocalSource,
    private val ctx: Context
): UserLocalSource {
    override suspend fun getUser(email: String): Result<User> {
        val entity = dao.getUser(email) ?: return Util.noEntityFailResult()
        val locRes = localLocationSrc.getLocationById(entity.id)
        if(locRes !is Success) return locRes as Fail
        val data = entity.toModel(locRes.data)
        return Success(data, 0)
    }

    override suspend fun getUserById(id: Int): Result<User> {
        val entity = dao.getUserById(id) ?: return Util.noEntityFailResult()
        val locRes = localLocationSrc.getLocationById(entity.id)
        if(locRes !is Success) return locRes as Fail
        val data = entity.toModel(locRes.data)
        return Success(data, 0)
    }

    override suspend fun updateUser(oldEmail: String, newData: User, newPswd: String): Result<Boolean> {
        val userEntity = dao.getUser(oldEmail) ?: return Util.noEntityFailResult()
        val userId = userEntity.id
        val newEntity = newData.toEntity(userId)
        return if(dao.updateUser(newEntity) > 0) {
            saveEmail(newData.email)
            Success(true, 0)
        } else Util.cantUpdateFailResult()
    }

    override suspend fun saveUser(data: User): Result<Boolean> {
        var entity = data.toEntity()
        val existingUser = dao.getUser(data.email)
        if(existingUser != null)
            entity = entity.copy(id = existingUser.id)
        val insertedRowId = dao.saveUser(entity)
        return if(insertedRowId >= 0L) Success(true, 0)
        else Util.cantInsertFailResult()
    }

    override suspend fun getCurrentUser(): Result<User> {
        val email = Util.getSharedPref(ctx).getString(Const.KEY_USER_EMAIL, null)
            ?: return Util.noValueFailResult()
        return getUser(email)
    }

    override suspend fun deleteCurrentUser(): Result<Boolean> {
        Util.editSharedPref(ctx) {
            remove(Const.KEY_USER_EMAIL)
            remove(Const.KEY_PASSWORD)
        }
        return Success(true, 0)
    }

    override suspend fun savePassword(password: String): Result<Boolean> {
        Util.editSharedPref(ctx, true) {
            putString(Const.KEY_PASSWORD, password)
        }
        return Success(true, 0)
    }

    override suspend fun saveEmail(email: String): Result<Boolean> {
        Util.editSharedPref(ctx, true) {
            putString(Const.KEY_USER_EMAIL, email)
        }
        return Success(true, 0)
    }

    override suspend fun getPassword(): Result<String> {
        val pswd = Util.getSharedPref(ctx).getString(Const.KEY_PASSWORD, null) ?: return Util.noValueFailResult()
        return Success(pswd, 0)
    }

    //private suspend fun getCurrentLocation(): Result<Location> = localLocationSrc.getCurrentLocation()
}