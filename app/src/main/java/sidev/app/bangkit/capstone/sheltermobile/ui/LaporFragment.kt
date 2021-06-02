package sidev.app.bangkit.capstone.sheltermobile.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.RiwayatLaporAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentLaporBinding


class LaporFragment : Fragment() {

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
        viewModel.getReportList(5,false)
    }

    private fun riwayatLaporRV() {
        binding.rvCard.layoutManager =
            LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        adapter = RiwayatLaporAdapter {  } //TODO Alif 2 Juni 2021
        binding.rvCard.adapter = adapter
    }

    private fun toTheMessage() {
        binding.cardHubungiTeks.setOnClickListener{
            val intent = Intent(requireContext(), LaporPesanFragment::class.java)
            startActivity(intent)
        }
    }

    private fun telephoneCall() {
        val numberTextSatgas = "0618468469"
        binding.ivCallEmergency.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numberTextSatgas"))
            startActivity(intent)
        }
    }



}