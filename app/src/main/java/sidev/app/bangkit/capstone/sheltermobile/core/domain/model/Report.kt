package sidev.app.bangkit.capstone.sheltermobile.core.domain.model

import java.sql.Timestamp

data class Report(
    val timestamp: String,
    val method: Int,
    val location: Location,
    val form: Form?, //null if `method` == Const.METHOD_CALL
)