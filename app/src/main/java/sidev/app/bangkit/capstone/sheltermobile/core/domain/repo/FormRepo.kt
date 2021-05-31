package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form

interface FormRepo {
    suspend fun getForm(id: Int): Result<Form>
    suspend fun saveForm(data: Form): Result<Boolean>
}