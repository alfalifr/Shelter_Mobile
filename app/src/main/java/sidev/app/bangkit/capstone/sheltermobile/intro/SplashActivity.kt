package sidev.app.bangkit.capstone.sheltermobile.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.LocationViewModel
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivitySplashBinding
import sidev.app.bangkit.capstone.sheltermobile.ui.LoginActivity
import sidev.lib.android.std.tool.util.`fun`.startAct

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var locVm : LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locVm = ViewModelDi.getLocationViewModel(this).apply {
            locationList.observe(this@SplashActivity) {
                if(it != null) {
                    startAct<LoginActivity>()
                    finish()
                }
            }
        }

        binding.pb.visibility = View.GONE

        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.shelterLogo.startAnimation(topAnimation)
        binding.tvShelterSlogan.startAnimation(bottomAnimation)

        val splashScreenTimeOut = 3500

        Handler().postDelayed(
            {
                locVm.getLocations()
                runOnUiThread { binding.pb.visibility = View.VISIBLE }
            }, splashScreenTimeOut.toLong()
        )
    }
}