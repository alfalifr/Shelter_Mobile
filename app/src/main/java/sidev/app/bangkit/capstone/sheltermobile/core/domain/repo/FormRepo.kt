package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import java.sql.Timestamp

interface FormRepo {
    suspend fun getForm(timestamp: String): Result<Form>
    suspend fun saveForm(data: Form): Result<Boolean>
}