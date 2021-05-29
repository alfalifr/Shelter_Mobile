package sidev.app.bangkit.capstone.sheltermobile.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import sidev.app.bangkit.capstone.sheltermobile.MainActivity
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        binding.shelterLogo.startAnimation(topAnimation)
        binding.tvShelterSlogan.startAnimation(bottomAnimation)

        val splashScreenTimeOut = 3500

        val homeIntent = Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeOut.toLong())

    }
}