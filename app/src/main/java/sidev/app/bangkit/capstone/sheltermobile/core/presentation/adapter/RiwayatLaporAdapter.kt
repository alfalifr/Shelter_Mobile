package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report

class RiwayatLaporAdapter(private val listener: (Report) -> Unit) : RecyclerView.Adapter<RiwayatLaporAdapter.LaporViewHolder>() {


    private var list: List<Report> = ArrayList()
    fun setList(report: List<Report>){
        list = report
        notifyDataSetChanged()
    }

    inner class LaporViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int){
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LaporViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}