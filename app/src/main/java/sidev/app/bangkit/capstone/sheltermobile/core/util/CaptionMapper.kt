package sidev.app.bangkit.capstone.sheltermobile.core.util

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.BasicCaption
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Emergency
import java.util.*

object CaptionMapper {
    object WarningStatus {
        fun getCaption(disaster: Disaster, emergency: Emergency): BasicCaption {
            val (title: String, desc: String) = when (disaster.name.toLowerCase(Locale.ROOT)) {
                Const.Disaster.FLOOD.toLowerCase(Locale.ROOT) -> when (emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Bersyukurlah, Anda di tempat Aman" to "Mari jaga lingkungan"
                    Const.Emergency.SEVERITY_YELLOW -> "Tetap tenang dan waspada" to "Lapor jika tanda bencana mengancam"
                    Const.Emergency.SEVERITY_RED -> "Mari bersiap untuk evakuasi" to "Siapkan diri dan keluarga"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.FOREST_FIRE.toLowerCase(Locale.ROOT) -> when (emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Bersyukurlah, Anda di tempat Aman" to "Mari jaga lingkungan"
                    Const.Emergency.SEVERITY_YELLOW -> "Tetap tenang dan waspada" to "Lapor jika tanda bencana mengancam"
                    Const.Emergency.SEVERITY_RED -> "Mari bersiap untuk evakuasi" to "Siapkan diri dan keluarga"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.EARTH_QUAKE.toLowerCase(Locale.ROOT) -> when (emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Bersyukurlah, Anda di tempat Aman" to "Mari jaga lingkungan"
                    Const.Emergency.SEVERITY_YELLOW -> "Tetap tenang dan waspada" to "Lapor jika tanda bencana mengancam"
                    Const.Emergency.SEVERITY_RED -> "Mari bersiap untuk evakuasi" to "Siapkan diri dan keluarga"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.Disaster.LANDSLIDE.toLowerCase(Locale.ROOT) -> when (emergency.severity) {
                    Const.Emergency.SEVERITY_GREEN -> "Bersyukurlah, Anda di tempat Aman" to "Mari jaga lingkungan"
                    Const.Emergency.SEVERITY_YELLOW -> "Tetap tenang dan waspada" to "Lapor jika tanda bencana mengancam"
                    Const.Emergency.SEVERITY_RED -> "Mari bersiap untuk evakuasi" to "Siapkan diri dan keluarga"
                    else -> throw IllegalArgumentException("No such emergency level ($emergency)")
                }
                Const.NO_NAME -> "Bersyukurlah, Anda di tempat Aman" to "Mari jaga lingkungan"
                else -> throw IllegalArgumentException("No such disaster data ($disaster)")
            }
            return BasicCaption(title, desc)
        }
    }
}