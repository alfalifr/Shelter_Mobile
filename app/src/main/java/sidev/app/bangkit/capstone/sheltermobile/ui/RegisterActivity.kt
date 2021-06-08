package sidev.app.bangkit.capstone.sheltermobile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.MainActivity
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.AuthData
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.LocationAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.AuthViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.LocationViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityRegisterBinding
import sidev.app.bangkit.capstone.sheltermobile.databinding.DialogLocationBinding
import sidev.lib.android.std.tool.util.`fun`.asResNameOrNullBy
import sidev.lib.android.std.tool.util.`fun`.findViewByType
import sidev.lib.android.std.tool.util.`fun`.startAct
import sidev.lib.android.std.tool.util.`fun`.toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dialogBinding: DialogLocationBinding
    private lateinit var dialog: AlertDialog
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var model: AuthViewModel
    private lateinit var user: User

    private var location: Location?= null

    private var name: String = ""
    private var email: String = ""
    private var pass: String = ""
    private var gender: Char = '_'
    private var confirm_pass: String = ""
    private val isLocValid: Boolean get()= location != null
    private var isNameValid = false
    private var isEmailValid = false
    private var isPswdValid = false
    private var isRePswdValid = false
    private val isGenderValid: Boolean get() = gender == Const.GENDER_MALE || gender == Const.GENDER_FEMALE
    private val isAllValid: Boolean get() = isNameValid && isEmailValid && isPswdValid && isRePswdValid && isGenderValid && isLocValid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        binding.onLoginPlus.startAnimation(anim)
        binding.onLoginWord.startAnimation(anim)

        binding.apply {
            tvChangeLocation.setOnClickListener { dialog.show() }
            showSignupLoading(false)
            cirRegisterButton.setOnClickListener {
                signup()
            }
            tvErrorAccount.visibility = View.GONE
            editTextName.addTextChangedListener {
                if (it != null) {
                    isNameValid = Util.validateName(it.toString())
                    if (isNameValid) {
                        editTextName.error = null
                    } else {
                        editTextName.error = getString(R.string.name_cant_be_blank)
                    }
                }
            }
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
                    pass = it.toString()
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
                    if (!isRePswdValid) {
                        editTextConfirmPassword.error = "Password yang di masukkan tidak sama"
                    } else {
                        editTextConfirmPassword.error = null
                    }

                }
            }
            groupGender.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radio_pria -> gender = Const.GENDER_MALE
                    R.id.radio_wanita -> gender = Const.GENDER_FEMALE
                    else -> throw IllegalStateException("No such view id for gender (${checkedId asResNameOrNullBy this@RegisterActivity})")
                }
            }
            onLoginPlus.setOnClickListener { finish() }
        }

        model = ViewModelDi.getAuthViewModel(this).apply {
            onPreAsyncTask {
                when (it) {
                    Const.KEY_SIGNUP -> showSignupLoading()
                }
            }
            onAuth.observe(this@RegisterActivity) {
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
                        startAct<MainActivity> {
                            it.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        finish()
                    } else {
                        binding.tvErrorAccount.visibility =
                            View.VISIBLE
                    }
                    showSignupLoading(false)
                }
            }
            currentLocation.observe(this@RegisterActivity) {
                location = it
                if(it != null) {
                    binding.tvLocationSet.text = it.name
                }
            }
            getCurrentLocation()
        }

        //Dialogue for Location
        initDialog()

        locationViewModel = ViewModelDi.getLocationViewModel(this).apply {
            onPreAsyncTask {
                when (it) {
                    Const.KEY_LOCATION_LIST -> showLocationLoading()
                }
            }
            locationList.observe(this@RegisterActivity) {
                if (it != null) {
                    locationAdapter.dataList = it
                    showLocationLoading(false)
                    showNoLocation(it.isEmpty())
                }
            }
            onSaveCurrentLocation.observe(this@RegisterActivity) {
                if (it != null) {
                    if (it) {
                        model.getCurrentLocation(true)
                        toast("Lokasi berhasil dipilih")
                    } else {
                        toast("Terjadi kesalahan saat menentukan lokasi")
                    }
                }
            }
            getLocations()
        }
    }

    private fun initDialog() {
        locationAdapter = LocationAdapter().apply {
            onItemClick {
                locationViewModel.saveCurrentLocation(it)
                dialog.cancel()
            }
        }
        dialogBinding = DialogLocationBinding.inflate(layoutInflater).apply {
            rvRowLocation.apply {
                adapter = locationAdapter
                layoutManager = LinearLayoutManager(this@RegisterActivity)
            }
            search.apply {
                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean =
                        if (query == null) false
                        else {
                            if (query.isNotBlank())
                                locationAdapter.filter { it.name.contains(query, true) }
                            else
                                locationAdapter.reset()
                            true
                        }

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
                val edit = findViewByType<EditText>()!!
                findViewById<View>(R.id.search_close_btn)!!.setOnClickListener {
                    edit.setText("")
                    locationAdapter.reset()
                }
            }
        }
        dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
    }

    private fun signup() {
        if (!isAllValid) return

        name = binding.editTextName.text.toString()
        email = binding.editTextEmail.text.toString()
        pass = binding.editTextPassword.text.toString()
        confirm_pass = binding.editTextConfirmPassword.text.toString()

        user = User(email, name, gender, location!!)
        val data = AuthData(email, pass)
        model.signup(user, data)
    }

    private fun showSignupLoading(show: Boolean = true) {
        binding.apply {
            if (show) {
                pbSignup.visibility = View.VISIBLE
                cirRegisterButton.visibility = View.GONE
                onLoginPlus.visibility = View.GONE
                onLoginWord.visibility = View.GONE
            } else {
                pbSignup.visibility = View.GONE
                cirRegisterButton.visibility = View.VISIBLE
                onLoginPlus.visibility = View.VISIBLE
                onLoginWord.visibility = View.VISIBLE
            }
        }
    }

    private fun showLocationLoading(show: Boolean = true) {
        dialogBinding.apply {
            if (show) {
                pb.visibility = View.VISIBLE
                rvRowLocation.visibility = View.GONE
            } else {
                pb.visibility = View.GONE
                rvRowLocation.visibility = View.VISIBLE
            }
        }
    }

    private fun showNoLocation(show: Boolean = true) {
        dialogBinding.apply {
            if (show) {
                tvNoData.visibility = View.VISIBLE
                rvRowLocation.visibility = View.GONE
            } else {
                tvNoData.visibility = View.GONE
                rvRowLocation.visibility = View.VISIBLE
            }
        }
    }

}
