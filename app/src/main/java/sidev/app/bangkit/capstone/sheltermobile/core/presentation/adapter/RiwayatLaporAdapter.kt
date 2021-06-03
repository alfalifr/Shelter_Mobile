package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.HistoryLaporListBinding
import sidev.app.bangkit.capstone.sheltermobile.ui.RiwayatLaporFormDetilActivity
import sidev.app.bangkit.capstone.sheltermobile.ui.RiwayatLaporTeleponDetilActivity
import sidev.lib.android.std.tool.util.`fun`.startAct
import java.lang.IllegalStateException

class RiwayatLaporAdapter(
//private val listener: (Report) -> Unit
) : RecyclerView.Adapter<RiwayatLaporAdapter.LaporViewHolder>() {


    private var list: List<Report> = ArrayList()
    fun setList(report: List<Report>) {
        list = report
        notifyDataSetChanged()
    }

    inner class LaporViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {

            val shelter = list[position]
/*
            itemView.setOnClickListener {
                listener(list[adapterPosition])
            }
 */

            val binding = HistoryLaporListBinding.bind(itemView)
            val tvDateForecast = binding.tvDateForecast
            val tvGenreLapor = binding.tvGenreLapor
            val tvLokasi = binding.tvLocation

            binding.root.setOnClickListener {
                it.context.apply {
                    when(val method = shelter.method) {
                        Const.METHOD_CALL -> startAct<RiwayatLaporTeleponDetilActivity>(Const.KEY_DATA to shelter)
                        Const.METHOD_FORM -> startAct<RiwayatLaporFormDetilActivity>(Const.KEY_DATA to shelter)
                        else -> throw IllegalStateException("No such report method ($method)")
                    }
                }
            }


            tvDateForecast.text = Util.getDateStr(shelter.timestamp)
            tvGenreLapor.text = when(shelter.method){
                Const.METHOD_CALL -> "Siaga Lapor Telepon"
                Const.METHOD_FORM -> "Siaga Lapor Pesan"
                else -> throw IllegalStateException("Invalid method")
            }
            tvLokasi.text = shelter.location.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporViewHolder {
        val view : View =
            LayoutInflater.from(parent.context).inflate(R.layout.history_lapor_list,parent,false)
        return LaporViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}