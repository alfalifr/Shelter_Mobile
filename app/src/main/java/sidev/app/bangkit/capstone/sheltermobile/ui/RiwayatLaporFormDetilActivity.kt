package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.setImg
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityRiwayatPesanDetilBinding
import sidev.lib.android.std.tool.util.`fun`.loge

class RiwayatLaporFormDetilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPesanDetilBinding
    private lateinit var vm: ReportViewModel
    private lateinit var data: Report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPesanDetilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getParcelableExtra(Const.KEY_DATA)!!

        binding.apply {
            tvJenisAksesLaporan.text = DataMapper.getMethodName(data.method)
            onLoginPlus.setOnClickListener { finish() }
        }

        vm = ViewModelDi.getReportViewModel(this).apply {
            reportDetail.observe(this@RiwayatLaporFormDetilActivity) {
                if(it != null) {
                    binding.apply {
                        tvDateDetail.text = Util.getDateWithDayStr(it.report.timestamp)
                        tvLocationSet.text = it.report.location.name
                        //val resp = it.response
                        //tvIsiDetilNote.text = if(resp.isBlank()) "<${getString(R.string.no_data)}>" else resp

                        tvIsiJudul.text = it.report.form!!.title
                        tvIsiDetilLaporan.text = it.report.form.desc

                        ivPict.visibility = View.GONE

                        val imgList = it.report.form.photoLinkList
                        if(imgList.isNotEmpty()) {
                            val link = imgList.first()
                            if(link.isNotBlank()) {
                                //val isImgUr = "i.imgur.com" in link
                                //loge("FormDetail imgLink = $link isImgUr= $isImgUr")
                                ivPict.visibility = View.VISIBLE
                                ivPict.setImg(imgList.first())
                            }
                        }
                    }
                }
            }
            getReportDetail(data.timestamp.time)
        }
        //tv_date_detail
    }
}