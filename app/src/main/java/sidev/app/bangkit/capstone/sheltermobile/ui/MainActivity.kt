package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.setWithBnv
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vpAdp: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vpAdp = MainViewPagerAdapter(this)

        binding.apply {
            vp.apply {
                setWithBnv(
                    bottomNavigation,
                    R.id.home,
                    R.id.emergency,
                    R.id.literatur,
                    R.id.profil,
                )
                adapter = vpAdp
                isUserInputEnabled = false
            }
        }
    }
}