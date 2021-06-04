package sidev.app.bangkit.capstone.sheltermobile.core.util

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.BasicCaption
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import java.util.*

object CaptionMapper {
    object WarningStatus {
        fun getCaption(disaster: Disaster, emergency: Emergency): BasicCaption {
            val (title: String, desc: String) = when(disaster.name.toLowerCase(Locale.ROOT)) {
                Const.Disaster.FLOOD.toLowerCase(Locale.ROOT) -> when(emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Alhamdulillah" to "Bisa tidur nyenyak"
                    Const.Emergency.SEVERITY_YELLOW -> "Waduh" to "Masih bisa siap2"
                    Const.Emergency.SEVERITY_RED -> "Hati2 bro" to "Gak bisa tidur"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.FOREST_FIRE.toLowerCase(Locale.ROOT) -> when(emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Alhamdulillah 2" to "Bisa tidur nyenyak"
                    Const.Emergency.SEVERITY_YELLOW -> "Waduh 2" to "Masih bisa siap2"
                    Const.Emergency.SEVERITY_RED -> "Hati2 bro 2" to "Gak bisa tidur"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.EARTH_QUAKE.toLowerCase(Locale.ROOT) -> when(emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Alhamdulillah 3" to "Bisa tidur nyenyak"
                    Const.Emergency.SEVERITY_YELLOW -> "Waduh 3" to "Masih bisa siap2"
                    Const.Emergency.SEVERITY_RED -> "Hati2 bro 3" to "Gak bisa tidur"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.LANDSLIDE.toLowerCase(Locale.ROOT) -> when(emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Alhamdulillah 4" to "Bisa tidur nyenyak"
                    Const.Emergency.SEVERITY_YELLOW -> "Waduh 4" to "Masih bisa siap2"
                    Const.Emergency.SEVERITY_RED -> "Hati2 bro 4" to "Gak bisa tidur"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                else -> throw IllegalArgumentException("No such disaster data ($disaster)")
            }
            return BasicCaption(title, desc)
        }
    }
}