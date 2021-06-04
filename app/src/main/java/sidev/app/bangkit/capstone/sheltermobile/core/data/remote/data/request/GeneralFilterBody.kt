package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

interface GeneralFilterBody: GeneralRequestBody {
    override val _requestType: String
    val filter: String
}