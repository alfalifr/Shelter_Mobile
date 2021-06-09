package sidev.app.bangkit.capstone.sheltermobile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.waitForValue
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityLaporPesanBinding
import sidev.lib.android.std.tool.util._BitmapUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.toast
import java.io.File


@Suppress("DEPRECATION")
class LaporPesanActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    private lateinit var binding: ActivityLaporPesanBinding
    private lateinit var model: ReportViewModel
    private var filePhoto: File? = null

    private var titleLaporan: String = ""
    private var isiLaporan: String = ""
    private var isTitleLaporanNotBlank = false
    private var isIsiLaporanNotBlank = false
    private var currentLocation: Location?= null


    private val isAllValid: Boolean get() = isTitleLaporanNotBlank && isIsiLaporanNotBlank


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loge("LaporPesanActivity.onCreate() binding.root= ${binding.root}")

        //call the function
        cameraFuction()
        laporPesan()
    }


    private fun laporPesan() {
        binding.apply {
            //includeLocation.ivLocation.te
            cardHubungiTeks.setOnClickListener {
                send()
            }
            textInputJudulLapor.addTextChangedListener {
                if (it != null) {
                    isTitleLaporanNotBlank = Util.validateFormTitle(it.toString())
                    if (isTitleLaporanNotBlank) {
                        textInputJudulLapor.error = null
                    } else {
                        textInputJudulLapor.error = "Judul laporan tidak boleh kosong"
                    }
                }
            }
            textInputLapor.addTextChangedListener {
                if (it != null) {
                    isIsiLaporanNotBlank = Util.validateFormDesc(it.toString())
                    if (isIsiLaporanNotBlank) {
                        textInputLapor.error = null
                    } else {
                        textInputLapor.error = "Isi laporan tidak boleh kosong"
                    }
                }
            }
        }

        model = ViewModelDi.getReportViewModel(this).apply {
            onCallNotSuccess { key, code, e ->
                when(key) {
                    Const.KEY_SEND_REPORT -> showUploadLoading(false)
                }
            }
            onPreAsyncTask {
                when(it) {
                    Const.KEY_SEND_REPORT -> showUploadLoading()
                }
            }
            onSend.observe(this@LaporPesanActivity) {
                if (it != null) {
                    showUploadLoading(false)
                    if (it) {
                        finish()
                        toast("Laporan anda sudah terkirim, akan kami proses")
                    } else {
                        toast("Terjadi kesalahan saat mengirim form.\nHarap kirim ulang.")
                    }
                }
            }
            currentLocation.observe(this@LaporPesanActivity) {
                if(it != null) {
                    this@LaporPesanActivity.currentLocation = it
                    binding.includeLocation.ivLocation.text = it.name
                }
            }
            getCurrentLocation()
        }

    }

    private fun send() {
        if (!isAllValid) return

        titleLaporan = binding.textInputJudulLapor.text.toString()
        isiLaporan = binding.textInputLapor.text.toString()

        val imgLinkList = if(filePhoto != null) listOf(filePhoto!!.absolutePath) else emptyList()
        val form = Form(Util.getTimeString(), titleLaporan, isiLaporan, imgLinkList)
        val location = currentLocation ?: run {
            model.getCurrentLocation()
            try {
                currentLocation = model.currentLocation.waitForValue()
                currentLocation!!
            } catch (e: Exception) {
                toast("Terjadi kesalahan saat mengambil data lokasi untuk form.\nHarap kirim ulang")
                return
            }
        }
        val data = Report(Util.getTimeString(), Const.METHOD_FORM, location, form)

        model.sendReport(data)
    }

    private fun cameraFuction() {
        binding.attachPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    (
                            this),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(
                    this,
                    "Ups anda menolak Shelter menggunakan kamera anda",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val thumbNail: Bitmap =
                    data?.extras?.get("data") as Bitmap //data can be from any type but we convert it to Bitmap hehe :p
                binding.ivImage.setImageBitmap(thumbNail)
                filePhoto = Util.getExternalFile(this, "foto_lapor.png")!!
                _BitmapUtil.savePict(thumbNail, filePhoto!!.parentFile!!.absolutePath, filePhoto!!.name)
            }
        }
    }

    private fun showUploadLoading(show: Boolean = true) {
        binding.apply {
            if(show){
                pbUpload.visibility = View.VISIBLE
                cardHubungiTeks.visibility = View.GONE
            } else {
                pbUpload.visibility = View.GONE
                cardHubungiTeks.visibility = View.VISIBLE
            }
        }
    }

}