package sidev.app.bangkit.capstone.sheltermobile.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.RiwayatLaporAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentLaporBinding
import sidev.lib.android.std.tool.util.`fun`.toast


class LaporFragment : Fragment() {
    companion object {
        const val REQ_CALL = 1
    }

    private lateinit var binding: FragmentLaporBinding
    private lateinit var adapter: RiwayatLaporAdapter
    private lateinit var viewModel : ReportViewModel


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

        //call the function
        telephoneCall()
        toTheMessage()
        riwayatLaporRV()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.reportHistory.observe(viewLifecycleOwner) { shelter ->
            if (shelter != null) {
                adapter.setList(shelter)
            }

        }
        viewModel.getReportList(5, false)
    }

    private fun riwayatLaporRV() {
        binding.rvCard.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        adapter = RiwayatLaporAdapter {  } //TODO Alif 2 Juni 2021
        binding.rvCard.adapter = adapter
    }

    private fun toTheMessage() {
        binding.cardHubungiTeks.setOnClickListener{
            val intent = Intent(requireContext(), LaporPesanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun telephoneCall() {
        binding.ivCallEmergency.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${Const.numberTextSatgas}"))
            if (Build.VERSION.SDK_INT < 23) {
                startActivity(intent)
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
                    startActivity(intent)
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
}