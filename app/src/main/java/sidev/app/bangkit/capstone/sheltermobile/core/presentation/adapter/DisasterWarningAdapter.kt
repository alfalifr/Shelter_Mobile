package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableList
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.setImg
import sidev.app.bangkit.capstone.sheltermobile.databinding.RowSiagaDetilExpandableBinding
import sidev.app.bangkit.capstone.sheltermobile.databinding.RowSiagaExpandableBinding
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.*


class DisasterViewHolder(private val binding: RowSiagaExpandableBinding): GroupViewHolder(binding.root) {
    fun bind(data: Disaster){
        binding.apply {
            tvSiaga1.text = data.name
            ivSiaga1.setImg(data.imgLink)
        }
    }
}
class WarningViewHolder(private val binding: RowSiagaDetilExpandableBinding): ChildViewHolder(binding.root) {
    fun bind(data: WarningStatus){
        binding.apply {
            tvDateForecast.text = Util.getDateStr(data.timestamp)
            tvZone.text = root.context.getString(R.string.zone_fill, data.emergency.name)
            val colorInt = Color.parseColor(data.emergency.color)
            cardZone.bgColorTint = colorInt
            ivBullet.colorTint = colorInt
        }
    }
}

class DisasterWarningAdapter(disasterWarnings: List<DisasterGroup>)
    : ExpandableRecyclerViewAdapter<DisasterViewHolder, WarningViewHolder>(disasterWarnings) {
/*
    var dataList: List<DisasterGroup>
        @Suppress(SuppressLiteral.UNCHECKED_CAST)
        get() = expandableList.groups as List<DisasterGroup>
        set(v){
            expandableList = ExpandableList(v)
            notifyDataSetChanged()
        }
 */

    /**
     * Called from [.onCreateViewHolder] when  the list item created is a group
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [GVH]  is being created
     * @return A [GVH] corresponding to the group list item with the  `ViewGroup` parent
     */
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): DisasterViewHolder = DisasterViewHolder(
        RowSiagaExpandableBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
    )

    /**
     * Called from [.onCreateViewHolder] when the list item created is a child
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [CVH]  is being created
     * @return A [CVH] corresponding to child list item with the  `ViewGroup` parent
     */
    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): WarningViewHolder = WarningViewHolder(
        RowSiagaDetilExpandableBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
    )

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item
     * bound to is a  child.
     *
     *
     * Bind data to the [CVH] here.
     *
     * @param holder The `CVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the child
     * @param group The [ExpandableGroup] that the the child list item belongs to
     * @param childIndex the index of this child within it's [ExpandableGroup]
     */
    override fun onBindChildViewHolder(
        holder: WarningViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        //group: DisasterGroup?,
        childIndex: Int
    ) {
        loge("onBindChildViewHolder() childIndex= $childIndex flatPosition= $flatPosition group= $group")
        if(group != null && holder != null){
            val data = (group as DisasterGroup).warningStatusList[childIndex]
            holder.bind(data)
        }
    }

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item bound to is a
     * group
     *
     *
     * Bind data to the [GVH] here.
     *
     * @param holder The `GVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the group
     * @param group The [ExpandableGroup] to be used to bind data to this [GVH]
     */
    override fun onBindGroupViewHolder(
        holder: DisasterViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        loge("onBindGroupViewHolder() flatPosition= $flatPosition group= $group")
        if(group != null && holder != null){
            val data = (group as DisasterGroup).disaster
            holder.bind(data)
        }
    }
}