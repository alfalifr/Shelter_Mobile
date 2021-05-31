package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success

object FormRemoteSourceDummy: FormRemoteSource {
    override suspend fun getForm(timestamp: String): Result<Form> = Success(Dummy.formList.random(), 0)
    override suspend fun saveForm(data: Form): Result<Boolean> = Success(true, 0)
}