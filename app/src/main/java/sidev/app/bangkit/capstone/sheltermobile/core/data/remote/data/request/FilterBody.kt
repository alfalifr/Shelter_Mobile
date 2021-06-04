package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

data class FilterBody(
    override val _requestType: String,
    override val filter: String,
): GeneralFilterBody