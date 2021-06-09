package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.FormDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class FormLocalSourceImpl(
    private val dao: FormDao,
    private val userLocalSrc: UserLocalSource,
): FormLocalSource {
    override suspend fun getForm(timestamp: TimeString): Result<Form> {
        val entity = dao.getForm(timestamp.timeLong) ?: return Util.noEntityFailResult()
        return Success(entity.toModel(), 0)
    }

    override suspend fun saveForm(data: Form): Result<Boolean> {
        val uidRes = userLocalSrc.getCurrentUserId()
        if(uidRes !is Success)
            return uidRes as Fail
        val isSuccess = dao.saveForm(data.toEntity(uidRes.data)) >= 0L
        return if(isSuccess) Success(true, 0)
        else Util.cantInsertFailResult()
    }
}