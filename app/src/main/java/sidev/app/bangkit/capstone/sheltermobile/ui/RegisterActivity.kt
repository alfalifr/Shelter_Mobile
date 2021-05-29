package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.AuthViewModel
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var model : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}