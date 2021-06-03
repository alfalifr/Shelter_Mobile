package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.RiwayatLaporAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.databinding.PageRvBinding

class ReportHistoryListActivity: AppCompatActivity() {
    private lateinit var binding: PageRvBinding
    private lateinit var vm: ReportViewModel
    private lateinit var adp: RiwayatLaporAdapter

    /**
     * {@inheritDoc}
     *
     * Perform initialization of all fragments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageRvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adp = RiwayatLaporAdapter()

        binding.apply {
            rv.apply {
                adapter = adp
                layoutManager = LinearLayoutManager(this@ReportHistoryListActivity)
            }
        }

        vm = ViewModelDi.getReportViewModel(this).apply {
            onPreAsyncTask {
                when(it) {
                    Const.KEY_REPORT_LIST -> showLoading()
                }
            }
            reportHistory.observe(this@ReportHistoryListActivity) {
                if(it != null) {
                    adp.setList(it)
                    showLoading(false)
                    showNoData(it.isEmpty())
                }
            }
            getReportList()
        }
    }

    private fun showNoData(show: Boolean = true) {
        binding.apply {
            if(show) {
                tvNoData.visibility = View.VISIBLE
                rv.visibility = View.GONE
            } else {
                tvNoData.visibility = View.GONE
                rv.visibility = View.VISIBLE
            }
        }
    }
    private fun showLoading(show: Boolean = true) {
        binding.apply {
            if(show) {
                pb.visibility = View.VISIBLE
                rv.visibility = View.GONE
            } else {
                pb.visibility = View.GONE
                rv.visibility = View.VISIBLE
            }
        }
    }
}