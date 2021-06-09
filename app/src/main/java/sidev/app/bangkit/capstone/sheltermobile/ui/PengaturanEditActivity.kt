package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.User
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.LocationAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.LocationViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ProfileViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityPengaturanEditBinding
import sidev.app.bangkit.capstone.sheltermobile.databinding.DialogLocationBinding
import sidev.lib.android.std.tool.util.`fun`.*
import java.lang.IllegalStateException

class PengaturanEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPengaturanEditBinding
    private lateinit var dialogBinding : DialogLocationBinding
    private lateinit var dialog : AlertDialog
    private lateinit var locationAdp : LocationAdapter
    private lateinit var vm : ProfileViewModel
    private lateinit var locationVm : LocationViewModel

    private lateinit var location: Location
    private lateinit var currentUser: User
    private lateinit var oldPswd: String

    private var typedOldPswd: String? = null
    private var newPswd: String? = null
    private var newRePswd: String? = null


    private val isOldPswdValid get()= typedOldPswd == oldPswd
    private val isNewPswdValid: Boolean get()= newPswd?.isNotBlank() == true
    private val isNewRePswdValid: Boolean get()= newRePswd == newPswd
    private val isPswdChanged: Boolean
        get()= typedOldPswd?.isNotBlank() == true
                || newPswd?.isNotBlank() == true
                || newRePswd?.isNotBlank() == true

    private var isNameValid = true
    private var isEmailValid = true
    private val isAllValid: Boolean get()= isNameValid && isEmailValid
            && (!isPswdChanged || (isOldPswdValid && isNewPswdValid && isNewRePswdValid))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            showUploadLoading(false)
            tvChangeLocation.setOnClickListener { dialog.show() }
            editTextName.apply {
                addTextChangedListener {
                    isNameValid = Util.validateName(it.toString())
                    error = if(isNameValid) null else getString(R.string.name_cant_be_blank)
                }
            }
            editTextEmail.apply {
                addTextChangedListener {
                    isEmailValid = Util.validateEmail(it.toString())
                    error = if(isEmailValid) null else getString(R.string.fill_a_valid_email)
                }
            }
            editTextOldPassword.apply {
                addTextChangedListener {
                    typedOldPswd = it?.toString()
                    error = if(!isPswdChanged || isOldPswdValid) null else getString(R.string.fill_old_password)
                }
            }
            editTextPassword.apply {
                addTextChangedListener {
                    newPswd = it?.toString()
                    error = if(!isPswdChanged || isNewPswdValid) null else getString(R.string.fill_new_password)
                }
            }
            editTextConfirmPassword.apply {
                addTextChangedListener {
                    newRePswd = it?.toString()
                    error = if(!isPswdChanged || isNewRePswdValid) null else getString(R.string.password_confirmation_must_be_same)
                }
            }
            btnSave.setOnClickListener { saveProfile() }
            onLoginPlus.setOnClickListener { finish() }
        }

        vm = ViewModelDi.getProfileViewModel(this).apply {
            onCallNotSuccess { key, code, e ->
                when(key) {
                    Const.KEY_SAVE_CURRENT_USER -> showUploadLoading(false)
                }
            }
            onPreAsyncTask {
                when(it) {
                    Const.KEY_SAVE_CURRENT_USER -> showUploadLoading()
                }
            }
            currentUser.observe(this@PengaturanEditActivity) {
                if(it != null) {
                    binding.apply {
                        //gender = it.gender
                        editTextName.setText(it.name)
                        editTextEmail.setText(it.email)
                        groupGender.check(when(it.gender) {
                            Const.GENDER_MALE -> R.id.radio_pria
                            Const.GENDER_FEMALE -> R.id.radio_wanita
                            else -> throw IllegalStateException("No such gender (${it.gender})")
                        })
                    }
                    this@PengaturanEditActivity.currentUser = it
                }
            }
            currentPassword.observe(this@PengaturanEditActivity) {
                if(it != null) {
                    oldPswd = it
                }
            }
            currentLocation.observe(this@PengaturanEditActivity) {
                if(it != null) {
                    binding.tvLocationSet.text = it.name
                    this@PengaturanEditActivity.location = it
                    //this@PengaturanEditActivity.currentUser = this@PengaturanEditActivity.currentUser.copy(location = it)
                }
            }
            onSaveProfile.observe(this@PengaturanEditActivity) {
                if(it != null) {
                    if(it) {
                        finish()
                        toast("Berhasil memperbarui profil")
                    } else {
                        showUploadLoading(false)
                        toast("Terjadi kesalahan saat memperbarui profil.\nHarap ulangi.")
                    }
                }
            }
            getCurrentUser()
            getCurrentPassword()
            getCurrentLocation()
        }

        //call function
        initDialog()

        locationVm = ViewModelDi.getLocationViewModel(this).apply {
            onPreAsyncTask {
                when(it) {
                    Const.KEY_LOCATION_LIST -> showLocationLoading()
                }
            }
            locationList.observe(this@PengaturanEditActivity) {
                loge("locationList.observe() list= $it")
                if(it != null) {
                    locationAdp.dataList = it
                    showLocationLoading(false)
                    showNoLocation(it.isEmpty())
                }
            }
            onSaveCurrentLocation.observe(this@PengaturanEditActivity) {
                if(it != null) {
                    if(it){
                        vm.getCurrentLocation(true)
                        toast("Berhasil mengubah lokasi")
                    } else {
                        toast("Terjadi kesalahan saat mengubah lokasi")
                    }
                }
            }
            getLocations()
        }
    }

    private fun initDialog(){
        locationAdp = LocationAdapter().apply {
            onItemClick {
                locationVm.saveCurrentLocation(it)
                dialog.cancel()
            }
        }
        dialogBinding = DialogLocationBinding.inflate(layoutInflater).apply {
            rvRowLocation.apply {
                adapter = locationAdp
                layoutManager = LinearLayoutManager(this@PengaturanEditActivity)
            }
            search.apply {
                setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = if(query == null) false
                    else {
                        if(query.isNotBlank())
                            locationAdp.filter { it.name.contains(query, true) }
                        else
                            locationAdp.reset()
                        true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
                val et = findViewByType<EditText>()!!
                findViewById<View>(R.id.search_close_btn)!!.setOnClickListener {
                    et.setText("")
                    locationAdp.reset()
                }
            }
        }
        dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()
    }

    private fun showNoLocation(show: Boolean = true) {
        dialogBinding.apply {
            if(show) {
                tvNoData.visibility = View.VISIBLE
                rvRowLocation.visibility = View.GONE
            } else {
                tvNoData.visibility = View.GONE
                rvRowLocation.visibility = View.VISIBLE
            }
        }
    }
    private fun showLocationLoading(show: Boolean = true) {
        dialogBinding.apply {
            if(show) {
                pb.visibility = View.VISIBLE
                rvRowLocation.visibility = View.GONE
            } else {
                pb.visibility = View.GONE
                rvRowLocation.visibility = View.VISIBLE
            }
        }
    }

    private fun saveProfile() {
        loge("saveProfile() isAllValid= $isAllValid")
        if(!isAllValid) return
        binding.apply {
            val email = editTextEmail.text.toString()
            val name = editTextName.text.toString()
            val gender = when(val id = groupGender.checkedRadioButtonId) {
                R.id.radio_pria -> Const.GENDER_MALE
                R.id.radio_wanita -> Const.GENDER_FEMALE
                else -> throw IllegalStateException("No such view id for gender (${id asResNameOrNullBy this@PengaturanEditActivity})")
            }
            val sentPswd = if(isNewPswdValid) newPswd else null
            val user = User(email, name, gender, location)
            vm.saveCurrentUser(user, sentPswd) // TODO Alif 3 Juni 2021: Buat ganti pswd
        }
    }

    private fun showUploadLoading(show: Boolean = true) {
        binding.apply {
            if(show) {
                pbUpload.visibility = View.VISIBLE
                btnSave.visibility = View.GONE
            } else {
                pbUpload.visibility = View.GONE
                btnSave.visibility = View.VISIBLE
            }
        }
    }
}