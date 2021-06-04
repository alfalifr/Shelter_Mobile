package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import java.util.*

data class LandslideBody(
    override val filter: String
): GeneralFilterBody {
    override val _requestType: String = Const.Disaster.LANDSLIDE.toLowerCase(Locale.ROOT)
}