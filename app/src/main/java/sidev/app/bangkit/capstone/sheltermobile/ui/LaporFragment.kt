package sidev.app.bangkit.capstone.sheltermobile.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.RiwayatLaporAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentLaporBinding
import sidev.app.bangkit.capstone.sheltermobile.ui.listener.PhoneListener
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.startAct
import sidev.lib.android.std.tool.util.`fun`.toast


class LaporFragment : Fragment() {
    companion object {
        const val REQ_CALL = 1
    }

    private lateinit var binding: FragmentLaporBinding
    private lateinit var adapter: RiwayatLaporAdapter
    private lateinit var viewModel : ReportViewModel
    private lateinit var reportActResLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentLocation: Location

    private var reportHistoryCount = 0

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, add a [androidx.lifecycle.LifecycleObserver] on the
     * activity's Lifecycle, removing it when it receives the
     * [Lifecycle.State.CREATED] callback.
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reportActResLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.getReportList(Const.INT_TOP_REPORT_LIMIT, true)
            val data = it.data
            loge("Const.REQ_CALL result= $it")
            loge("Const.REQ_CALL data= $data")
            loge("Const.REQ_CALL data?.data= ${data?.data}")
            loge("Const.REQ_CALL data?.extras= ${data?.extras}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLaporBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelDi.getReportViewModel(this)
        binding.apply {
            tvLookFurther.setOnClickListener {
                startAct<ReportHistoryListActivity>()
            }
        }

        //call the function
        telephoneCall()
        //registerTelephony()
        toTheMessage()
        riwayatLaporRV()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.apply {
            onPreAsyncTask {
                when(it) {
                    Const.KEY_CURRENT_LOC -> binding.pbLocation.visibility = View.VISIBLE
                    Const.KEY_REPORT_LIST -> showLoading(binding.pbHistory, binding.rvCard)
                }
            }
            currentLocation.observe(viewLifecycleOwner) {
                if(it != null) {
                    this@LaporFragment.currentLocation = it
                    binding.includeLocation.ivLocation.text = it.name
                    binding.pbLocation.visibility = View.GONE
                }
            }
            reportHistory.observe(viewLifecycleOwner) { shelter ->
                loge("reportHistory.observe() data = $shelter")
                if (shelter != null) {
                    adapter.setList(shelter)
                    showLoading(binding.pbHistory, binding.rvCard, false)
                    showNoReportData(shelter.isEmpty())
                }
            }
            reportHistoryCount.observe(viewLifecycleOwner) {
                if(it != null) {
                    this@LaporFragment.reportHistoryCount = it
                }
            }
            getCurrentLocation()
            getReportList(Const.INT_TOP_REPORT_LIMIT)
        }
    }

    private fun riwayatLaporRV() {
        binding.rvCard.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        adapter = RiwayatLaporAdapter() //{  }
        binding.rvCard.adapter = adapter
    }

    private fun toTheMessage() {
        binding.cardHubungiTeks.setOnClickListener{
            val intent = Intent(requireContext(), LaporPesanActivity::class.java)
            reportActResLauncher.launch(intent)
            //startActivity(intent)
        }
    }

    private fun registerTelephony() {
        val tm = requireContext().getSystemService<TelephonyManager>()!!
        val listener = PhoneListener {
            val report = Report(Util.getTimeString(), Const.METHOD_CALL, currentLocation, null)
            viewModel.sendReport(report)
        }
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun telephoneCall() {
        binding.ivCallEmergency.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${Const.numberTextSatgas}"))
            if (Build.VERSION.SDK_INT < 23) {
                registerTelephony()
                reportActResLauncher.launch(intent)
                //requireActivity().startActivityForResult(intent, Const.REQ_CALL)
            } else {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        REQ_CALL
                    )
                } else {
                    //val PERMISSIONS_STORAGE = arrayOf<String>(Manifest.permission.CALL_PHONE)
                    //ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_STORAGE, 9)
                    //startActivity(intent)
                    //requireActivity().startActivityForResult(intent, Const.REQ_CALL)
                    registerTelephony()
                    reportActResLauncher.launch(intent)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQ_CALL){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //startActivityForResult(intent, LaporPesanFragment.CAMERA_REQUEST_CODE)
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${Const.numberTextSatgas}"))
                startActivity(intent)
            } else {
                toast("Ups anda menolak Shelter menggunakan kamera anda")
            }
        }
    }
/*
    /**
     * Receive the result from a previous call to
     * [.startActivityForResult].  This follows the
     * related Activity API as described there in
     * [Activity.onActivityResult].
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode The integer result code returned by the child activity
     * through its setResult().
     * @param data An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loge("onActivityResult() requestCode= $requestCode resultCode= $resultCode")
        loge("onActivityResult() data= $data")
        loge("onActivityResult() data?.data= ${data?.data}")
        loge("onActivityResult() data?.extras= ${data?.extras}")
        when(requestCode) {
            Const.REQ_CALL -> {
                loge("Const.REQ_CALL data= $data")
                loge("Const.REQ_CALL data?.data= ${data?.data}")
                loge("Const.REQ_CALL data?.extras= ${data?.extras}")
            }
        }
    }
 */

    private fun showNoReportData(show: Boolean = true) {
        binding.apply {
            if(show) {
                tvNoData.visibility = View.VISIBLE
                rvCard.visibility = View.GONE
                tvLookFurther.visibility = View.GONE
            } else {
                tvNoData.visibility = View.GONE
                rvCard.visibility = View.VISIBLE
                tvLookFurther.visibility =
                    if(reportHistoryCount > Const.INT_TOP_REPORT_LIMIT) View.VISIBLE
                    else View.GONE
            }
        }
    }
    private fun showLoading(pb: ProgressBar, loadedView: View, show: Boolean = true) {
        if(show) {
            pb.visibility = View.VISIBLE
            loadedView.visibility = View.GONE
        } else {
            pb.visibility = View.GONE
            loadedView.visibility = View.VISIBLE
        }
    }
}