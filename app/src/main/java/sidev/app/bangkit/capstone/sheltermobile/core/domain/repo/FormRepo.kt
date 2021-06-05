package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import java.sql.Timestamp

interface FormRepo {
    suspend fun getForm(timestamp: TimeString): Result<Form>
    suspend fun saveForm(data: Form): Result<Boolean>
}