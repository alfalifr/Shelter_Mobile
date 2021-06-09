package sidev.app.bangkit.capstone.sheltermobile.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.DisasterWarningAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.DashboardViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.CaptionMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.setImg
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentHomeBinding
import sidev.app.bangkit.capstone.sheltermobile.receiver.AlarmNotifReceiver
import sidev.lib.android.std.tool.util.`fun`.bgColorTint
import sidev.lib.android.std.tool.util.`fun`.toast


class HomeFragment : Fragment() {

    private inner class WarningReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            vm.apply {
                getHighlightedWarningStatus(true)
                getWeatherForecast(true)
                getDisasterGroupList(true)
            }
        }
    }

    private lateinit var binding : FragmentHomeBinding
    private lateinit var vm : DashboardViewModel
    private lateinit var disasterAdp : DisasterWarningAdapter
    private lateinit var warningReceiver : WarningReceiver
    private var initAlarm = false

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
            rvCard.apply {
                adapter = disasterAdp
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        vm.apply {
            onCallNotSuccess { key, code, e ->
                toast("Terjadi kesalahan, code = $code, e = $e")
            }
            onPreAsyncTask {
                when(it) {
                    Const.KEY_CURRENT_LOC -> {
                        binding.apply {
                            pbLocation.visibility = View.VISIBLE
                            ivLocation.text = ""
                        }
                    }
                    Const.KEY_DISASTER_GROUP_LIST -> showLoading(binding.pbWarningDisaster, binding.rvCard)
                    Const.KEY_WEATHER_FORECAST -> {
                        showLoading(binding.pbWeather, binding.vgWeather)
                        showLoading(binding.pbWeatherDetail, binding.vgWeatherDetail)
                    }
                    Const.KEY_WARNING_HIGHLIGHT -> showLoading(binding.pbWarningHighlight, binding.vgWarningHighlight)
                }
            }
            currentLocation.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.apply {
                        pbLocation.visibility = View.GONE
                        ivLocation.text = it.name
                    }
                }
            }
            weatherForecast.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.apply {
                        ivWeather.setImg(it.weather.icon)
                        tvHumaditySrc.text = Util.getFormattedStr(it.humidity, "%")
                        tvWindspeedSrc.text = Util.getFormattedStr(it.windSpeed, "km/jam")
                        tvRainfallSrc.text = Util.getFormattedStr(it.rainfall, "%")
                        tvDurationSrc.text = Util.getFormattedStr(it.ultraviolet)
                        tvTemperature.text = Util.getFormattedStr(it.temperature, afterComma = 0)
                        tvDay.text = Util.getDayName(it.timestamp)
                        tvDate.text = Util.getDateStr(it.timestamp)
                    }
                    showLoading(binding.pbWeather, binding.vgWeather, false)
                    showLoading(binding.pbWeatherDetail, binding.vgWeatherDetail, false)
                }
            }
            higlightedWarningStatus.observe(viewLifecycleOwner){
                if(it != null) {
                    binding.apply {
                        val caption = CaptionMapper.WarningStatus.getCaption(it.disaster, it.emergency)
                        ivNotification.setImg(it.disaster.imgLink)
                        tvTitleNotif.text = caption.title
                        tvNotif.text = Util.getFormattedStr(it)

                        cardNotifications.apply {
                            val color = Color.parseColor(it.emergency.color)
                            setBackgroundColor(color)
                            setShadowColorLight(color)
                            setShadowColorDark(color)
                        }
                        //cardNotifications.bgColorTint = Color.parseColor()
                    }
                    showLoading(binding.pbWarningHighlight, binding.vgWarningHighlight, false)
                    if(initAlarm){
                        if(it.emergency.severity != Const.Emergency.SEVERITY_GREEN){
                            val caption = CaptionMapper.WarningStatus.getCaption(it.disaster, it.emergency)
                            Util.showNotif(
                                requireContext(),
                                title = caption.title,
                                desc = caption.desc,
                            )
                        }
                    }
                }
            }
            disasterStatusList.observe(viewLifecycleOwner){
                if(it != null) {
                    //disasterAdp.dataList = it
                    disasterAdp = DisasterWarningAdapter(it)
                    binding.rvCard.adapter= disasterAdp
                    showLoading(binding.pbWarningDisaster, binding.rvCard, false)
                }
            }
            getCurrentLocation()
            getDisasterGroupList()
            getWeatherForecast()
            getHighlightedWarningStatus()
        }
        warningReceiver = WarningReceiver()
        initAlarm = AlarmNotifReceiver.setOn(requireContext())
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to [Activity.onResume] of the containing
     * Activity's lifecycle.
     */
    override fun onResume() {
        super.onResume()
        vm.getCurrentLocation(true)
        val intentFilter = IntentFilter(Const.ACTION_ALARM_NOTIF_ACT)
        requireActivity().registerReceiver(warningReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(warningReceiver)
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