package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import sidev.app.bangkit.capstone.sheltermobile.MainActivity
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.AuthViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityRegisterBinding
import sidev.lib.android.std.tool.util.`fun`.startAct

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var model: AuthViewModel
    private lateinit var user : User

    private var name: String = ""
    private var email: String = ""
    private var pass: String = ""
    private var gender: Char = '_'
    private var confirm_pass: String = ""
    private var isEmailValid = false
    private var isPswdValid = false
    private var isRePswdValid = false
    private val isGenderValid: Boolean get()= gender == Const.GENDER_MALE || gender == Const.GENDER_FEMALE
    private val isAllValid: Boolean get() = isEmailValid && isPswdValid && isRePswdValid && isGenderValid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        binding.onLoginPlus.startAnimation(anim)
        binding.onLoginWord.startAnimation(anim)

        binding.apply {
            cirRegisterButton.setOnClickListener {
                signup()
            }
            tvErrorAccount.visibility = View.GONE
            editTextEmail.addTextChangedListener {
                if (it != null) {
                    isEmailValid = Util.validateEmail(it.toString())
                    if (isEmailValid) {
                        editTextEmail.error = null
                    } else {
                        editTextEmail.error = "Email tidak valid"
                    }
                }
            }
            editTextPassword.addTextChangedListener {
                if (it != null) {
                    isPswdValid = Util.validatePassword(it.toString())
                    if (isPswdValid) {
                        editTextPassword.error = null
                    } else {
                        editTextPassword.error = "Password tidak valid"
                    }
                }
            }

            editTextConfirmPassword.addTextChangedListener {
                if (it != null) {
                    val rePswd = binding.editTextConfirmPassword.text.toString()
                    isRePswdValid = rePswd == pass
                    if (!isRePswdValid){
                        editTextConfirmPassword.error = "Password yang di masukkan tidak sama"
                    } else {
                        editTextConfirmPassword.error = null
                    }

                }
            }
            groupGender.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.radio_pria -> gender= Const.GENDER_MALE
                    R.id.radio_wanita -> gender= Const.GENDER_FEMALE
                }
            }
        }

        model = ViewModelDi.getAuthViewModel(this)
        model.onAuth.observe(this) {
            if (it != null) {
                if (it) {
                    binding.textInputEmail.visibility = View.GONE
/*
                    Util.editSharedPref(this) {
                        putString(Const.KEY_USER_EMAIL, email)
                        putString(Const.KEY_PASSWORD, pass)
                    }
                    model.saveUser(user)
 */
                    startAct<MainActivity>()
                } else {
                    binding.tvErrorAccount.visibility = View.VISIBLE //TODO Mella: ganti pake text error dari server contoh : akun yg dimasukkan tidak valid
                }
            }
        }
    }

    private fun signup() {
        if (!isAllValid) return

        name = binding.editTextName.text.toString()
        email = binding.editTextEmail.text.toString()
        pass = binding.editTextPassword.text.toString()
        confirm_pass = binding.editTextConfirmPassword.text.toString()


        user = User(email,name,gender)
        val data = AuthData(email,pass)
        model.signup(user,data)
    }

}
