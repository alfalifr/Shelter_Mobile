package sidev.app.bangkit.capstone.sheltermobile.core.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.parcelize.Parcelize
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus

@Parcelize
data class DisasterGroup(
    val disaster: Disaster,
    val warningStatusList: List<WarningStatus>,
): ExpandableGroup<WarningStatus>(disaster.name, warningStatusList)