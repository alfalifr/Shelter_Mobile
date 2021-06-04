package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class GeneralMinMaxBodyImpl(
    override val _requestType: String,
    val minDate: String,
    val maxDate: String,
): GeneralRequestBody