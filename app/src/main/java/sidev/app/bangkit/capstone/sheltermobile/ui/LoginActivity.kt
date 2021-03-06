package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.AuthViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityLoginBinding
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.startAct

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var model: AuthViewModel

    private var email: String = ""
    private var pswd: String = ""
    private var isEmailValid = false
    private var isPswdValid = false

    private val isAllValid: Boolean get() = isEmailValid && isPswdValid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        binding.onRegisterPlus.startAnimation(anim1)
        binding.onRegisterWord.startAnimation(anim1)


        binding.apply {
            showLoginLoading(false)
            cirLoginButton.setOnClickListener {
                login()
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
            val toSignupPageClickListener = View.OnClickListener { startAct<RegisterActivity>() }
            onRegisterWord.setOnClickListener(toSignupPageClickListener)
            onRegisterPlus.setOnClickListener(toSignupPageClickListener)
        }


        model = ViewModelDi.getAuthViewModel(this).apply {
            onPreAsyncTask {
                when(it) {
                    Const.KEY_LOGIN -> showLoginLoading()
                }
            }
            onAuth.observe(this@LoginActivity) {
                loge("LoginAct onAuth.observe() it= $it")
                if (it != null) {
                    if (it) {
                        binding.tvErrorAccount.visibility = View.GONE
/*
                    Util.editSharedPref(this) {
                        putString(Const.KEY_USER_EMAIL, email)
                        putString(Const.KEY_PASSWORD, pswd)
                    }
 */
                        //val email = Util.getSharedPref(this).getString(Const.KEY_USER_EMAIL, null)
                        startAct<MainActivity>()
                        finish()
                    } else {
                        binding.tvErrorAccount.visibility = View.VISIBLE
                    }
                    showLoginLoading(false)
                }
            }
            checkLoginStatus()
        }

    }

    private fun login() {
        if (!isAllValid) return

        email = binding.editTextEmail.text.toString()
        pswd = binding.editTextPassword.text.toString()

        val data = AuthData(email, pswd)
        model.login(data)
    }

    private fun showLoginLoading(show: Boolean = true) {
        binding.apply {
            if(show) {
                pbLogin.visibility = View.VISIBLE
                cirLoginButton.visibility = View.GONE
                onRegisterWord.visibility = View.GONE
                onRegisterPlus.visibility = View.GONE
            } else {
                pbLogin.visibility = View.GONE
                cirLoginButton.visibility = View.VISIBLE
                onRegisterWord.visibility = View.VISIBLE
                onRegisterPlus.visibility = View.VISIBLE
            }
        }
    }
}