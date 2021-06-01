package sidev.app.bangkit.capstone.sheltermobile.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.DisasterWarningAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.DashboardViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentHomeBinding
import sidev.lib.android.std.tool.util.`fun`.bgColorTint


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var vm : DashboardViewModel
    private lateinit var disasterAdp : DisasterWarningAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelDi.getDashboardViewModel(this)
        disasterAdp = DisasterWarningAdapter(emptyList())

        binding.apply {
            rvCard.adapter = disasterAdp
        }

        vm.apply {
            currentLocation.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.ivLocation.text = it.name
                }
            }
            weatherForecast.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.apply {
                        tvHumaditySrc.text = Util.getFormattedStr(it.humidity, "%")
                        tvWindspeedSrc.text = Util.getFormattedStr(it.windSpeed, "km/jam")
                        tvRainfallSrc.text = Util.getFormattedStr(it.rainfall, "%")
                        tvDurationSrc.text = Util.getFormattedStr(it.ultraviolet)
                    }
                }
            }
            higlightedWarningStatus.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.apply {
                        tvTitleNotif.text = it.title
                        tvNotif.text = Util.getFormattedStr(it)
                        cardNotifications.bgColorTint = Color.parseColor(it.emergency.color)
                    }
                }
            }
            disasterStatusList.observe(viewLifecycleOwner){
                if(it != null) {
                    disasterAdp.dataList = it
                }
            }
        }

        vm.getCurrentLocation()
        vm.getDisasterGroupList()
        vm.getWeatherForecast()
        vm.getHighlightedWarningStatus()
    }

    private fun showLoading(pb: ProgressBar, loadedView: View, show: Boolean = true) {
        if(show){
            pb.visibility = View.VISIBLE
            loadedView.visibility = View.GONE
        } else {
            pb.visibility = View.GONE
            loadedView.visibility = View.VISIBLE
        }
    }
}