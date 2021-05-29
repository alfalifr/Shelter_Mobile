package sidev.app.bangkit.capstone.sheltermobile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim1 = AnimationUtils.loadAnimation(this,R.anim.slide_in_right)
        binding.onRegisterPlus.startAnimation(anim1)
        binding.onRegisterWord.startAnimation(anim1)




    }




}