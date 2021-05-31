package sidev.app.bangkit.capstone.sheltermobile.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentLaporBinding


class LaporFragment : Fragment() {

    private lateinit var binding: FragmentLaporBinding


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

        //make a function
        telephoneCall()
        toTheMessage()
        riwayatLaporRV()
    }

    private fun riwayatLaporRV() {

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