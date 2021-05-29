package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.view.View
import android.view.ViewGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WarningStatus
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup


class DisasterViewHolder(view: View): GroupViewHolder(view) {
    fun bind(data: Disaster){

    }
}
class WarningViewHolder(view: View): ChildViewHolder(view) {
    fun bind(data: WarningStatus){

    }
}

class DisasterWarningAdapter(disasterWarnings: List<DisasterGroup>)
    : ExpandableRecyclerViewAdapter<DisasterViewHolder, WarningViewHolder>(disasterWarnings) {
    /**
     * Called from [.onCreateViewHolder] when  the list item created is a group
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [GVH]  is being created
     * @return A [GVH] corresponding to the group list item with the  `ViewGroup` parent
     */
    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): DisasterViewHolder {
        TODO("Not yet implemented")
    }

    /**
     * Called from [.onCreateViewHolder] when the list item created is a child
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [CVH]  is being created
     * @return A [CVH] corresponding to child list item with the  `ViewGroup` parent
     */
    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): WarningViewHolder {
        TODO("Not yet implemented")
    }

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
        if(group != null && holder != null){
            val data = (group as DisasterGroup).disaster
            holder.bind(data)
        }
    }
}