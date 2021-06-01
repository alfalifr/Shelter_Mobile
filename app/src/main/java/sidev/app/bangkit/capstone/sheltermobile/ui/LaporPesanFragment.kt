package sidev.app.bangkit.capstone.sheltermobile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Form
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Report
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ReportViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityLaporPesanBinding
import sidev.lib.android.std.tool.util._BitmapUtil
import sidev.lib.android.std.tool.util.`fun`.toast
import java.io.File


@Suppress("DEPRECATION")
class LaporPesanFragment : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    private lateinit var binding: ActivityLaporPesanBinding
    private lateinit var model: ReportViewModel
    private lateinit var filePhoto: File

    private var titleLaporan: String = ""
    private var isiLaporan: String = ""
    private var isTitleLaporanNotBlank = false
    private var isIsiLaporanNotBlank = false


    private val isAllValid: Boolean get() = isTitleLaporanNotBlank && isIsiLaporanNotBlank

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        //call the function
        cameraFuction()
        laporPesan()
    }


    private fun laporPesan() {

        binding.apply {
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

        model = ViewModelDi.getReportViewModel(this)
        model.onSend.observe(this) {
            if (it != null) {
                if (it) {
                    finish()
                    toast("Laporan anda sudah terkirim, akan kami proses")
                }
            }
        }

    }

    private fun send() {
        if (!isAllValid) return

        titleLaporan = binding.textInputJudulLapor.text.toString()
        isiLaporan = binding.textInputLapor.text.toString()

        val form = Form(Util.getTimestampStr(), titleLaporan, isiLaporan, listOf(filePhoto.absolutePath))
        val location = model.getCurrentLocation()
        val data = Report(Util.getTimestampStr(), Const.METHOD_FORM, location, form)

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
                _BitmapUtil.savePict(thumbNail, filePhoto.parentFile!!.absolutePath, filePhoto.name)
            }
        }
    }


}