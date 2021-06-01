package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.FormDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class FormLocalSourceImpl(private val dao: FormDao): FormLocalSource {
    override suspend fun getForm(timestamp: String): Result<Form> {
        val entity = dao.getForm(timestamp) ?: return Util.noEntityFailResult()
        return Success(entity.toModel(), 0)
    }

    override suspend fun saveForm(data: Form): Result<Boolean> {
        val isSuccess = dao.saveForm(data.toEntity()) >= 0L
        return if(isSuccess) Success(true, 0)
        else Util.cantInsertFailResult()
    }
}