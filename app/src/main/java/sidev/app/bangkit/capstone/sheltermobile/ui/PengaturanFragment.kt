package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.ProfileViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util.setGenderImg
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentProfilBinding
import sidev.lib.android.std.tool.util.`fun`.startAct
import sidev.lib.android.std.tool.util.`fun`.toast

class PengaturanFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var vm: ProfileViewModel
    private var isFromEdit = false
/*
    private lateinit var editActResLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editActResLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isFromEdit = true
            startAct<PengaturanEditActivity>()
        }
    }
 */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cardEditProfile.setOnClickListener {
                isFromEdit = true
                startAct<PengaturanEditActivity>()
            }
            cardLogout.setOnClickListener {
                vm.logout()
            }
        }

        vm = ViewModelDi.getProfileViewModel(this).apply {
            onPreAsyncTask {
                binding.apply {
                    when(it) {
                        Const.KEY_CURRENT_USER -> {
                            showLoading(pbPhoto, ivUser)
                            showLoading(pbInfo, vgInfo)
                        }
                        Const.KEY_CURRENT_LOC -> showLoading(pbLocation, tvLocation)
                    }
                }
            }
            currentUser.observe(viewLifecycleOwner) {
                if(it != null) {
                    binding.apply {
                        tvName.text = it.name
                        tvGender.text = DataMapper.getGenderName(it.gender)
                        tvEmail.text = it.email
                        ivUser.setGenderImg(it.gender)
                        showLoading(pbPhoto, ivUser, false)
                        showLoading(pbInfo, vgInfo, false)
                    }
                }
            }
            currentLocation.observe(viewLifecycleOwner) {
                if(it != null) {
                    binding.apply {
                        tvLocation.text = it.name
                        showLoading(pbLocation, tvLocation, false)
                    }

                }
            }
            onLogout.observe(viewLifecycleOwner) {
                //loge("onLogout.observe it= $it")
                if(it != null) {
                    if(it) {
                        startAct<LoginActivity>()
                        requireActivity().finish()
                    } else {
                        toast("Gagal logout")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.apply {
            getCurrentUser(isFromEdit)
            getCurrentLocation(isFromEdit)
            if(isFromEdit)
                isFromEdit = false
        }
    }

    private fun showLoading(pb: ProgressBar, loadedView: View, show: Boolean = true) {
        if(show) {
            pb.visibility = View.VISIBLE
            loadedView.visibility = View.GONE
        } else {
            pb.visibility = View.GONE
            loadedView.visibility = View.VISIBLE
        }
    }

}
/*
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PengaturanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PengaturanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PengaturanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PengaturanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
 */