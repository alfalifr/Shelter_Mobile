package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import java.util.*

data class FloodBody(
    override val filter: String
): GeneralFilterBody {
    override val _requestType: String = Const.Disaster.FLOOD.toLowerCase(Locale.ROOT)
}