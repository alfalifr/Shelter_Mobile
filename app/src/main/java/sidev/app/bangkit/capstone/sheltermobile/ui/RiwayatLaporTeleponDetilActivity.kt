package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityRiwayatLaporDetilBinding
import sidev.lib.android.std.tool.util.`fun`.loge

class RiwayatLaporTeleponDetilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatLaporDetilBinding
    private lateinit var vm: ReportViewModel
    private lateinit var data: Report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatLaporDetilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(Const.KEY_DATA)!!
        binding.apply {
            tvDateDetail.text = Util.getDateWithDayStr(data.timestamp)
            tvLocationSet.text = data.location.name
            tvJenisAksesLaporan.text = DataMapper.getMethodName(data.method)
            onLoginPlus.setOnClickListener { finish() }
            //tvIsiDetilNote.text
        }

        vm = ViewModelDi.getReportViewModel(this).apply {
            reportDetail.observe(this@RiwayatLaporTeleponDetilActivity) {
                loge("reportDetail.observe() detail = $it")
/*
                if(it != null) {
                    val resp = it.response
                    binding.tvIsiDetilNote.text = if(resp.isBlank()) "<${getString(R.string.no_data)}>" else resp
                }
 */
            }
            getReportDetail(data.timestamp.time)
        }
    }
}