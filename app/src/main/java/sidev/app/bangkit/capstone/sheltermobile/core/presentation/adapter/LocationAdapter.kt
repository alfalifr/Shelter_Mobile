package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.databinding.RowLocationDialogBinding

class LocationAdapter: RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: RowLocationDialogBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Location) {
            binding.apply {
                detilLocation.text = data.name
                root.setOnClickListener {
                    onItemClick?.invoke(data)
                }
            }
        }
    }

    private var onItemClick: ((data: Location) -> Unit)?= null
    fun onItemClick(l: ((data: Location) -> Unit)?) {
        onItemClick = l
    }

    private var isInternalEdit = false
    var dataList: List<Location>? = null
        set(v){
            field = v
            if(!isInternalEdit)
                fullDataList = v
            notifyDataSetChanged()
        }
    private var fullDataList: List<Location>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RowLocationDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList!![position])

    override fun getItemCount(): Int = dataList?.size ?: 0

    fun filter(predicate: (Location) -> Boolean) {
        isInternalEdit = true
        dataList = fullDataList?.filter(predicate)
        isInternalEdit = false
    }

    fun reset() {
        isInternalEdit = true
        dataList = fullDataList
        isInternalEdit = false
    }
}