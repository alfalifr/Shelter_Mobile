package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

interface GeneralMinMaxBody: GeneralRequestBody {
    override val _requestType: String
    val minDate: String
    val maxDate: String
}