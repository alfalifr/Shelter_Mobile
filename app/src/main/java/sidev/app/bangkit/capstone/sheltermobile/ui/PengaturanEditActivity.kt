package sidev.app.bangkit.capstone.sheltermobile.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityPengaturanEditBinding

class PengaturanEditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPengaturanEditBinding

    private lateinit var btn : Button
    internal lateinit var myDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //call function
        dialogueLocation()
    }

    private fun dialogueLocation() {
        btn = binding.tvChangeLocation as Button
        btn.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {

    }
}