package sidev.app.bangkit.capstone.sheltermobile.core.presentation.model

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus

data class DisasterGroup(
    val disaster: Disaster,
    val warningStatusList: List<WarningStatus>,
): ExpandableGroup<WarningStatus>(disaster.name, warningStatusList)