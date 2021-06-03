package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.ArticleNewsAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.SearchArticleViewModel
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivitySearchBinding
import sidev.lib.android.std.tool.util.`fun`.findViewByType

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var vm: SearchArticleViewModel
    private lateinit var adp: ArticleNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adp = ArticleNewsAdapter()

        vm = ViewModelDi.getSearchArticleViewModel(this)
        vm.apply {
            onPreAsyncTask {
                showLoading()
            }
            searchResult.observe(this@SearchActivity) {
                adp.dataList = it
                showLoading(false)
                showNoData(it?.isNotEmpty() == false)
            }
        }

        binding.apply {
            pb.visibility = View.GONE
            tvNoData.visibility = View.GONE
            rvNews.apply {
                adapter = adp
                layoutManager = LinearLayoutManager(this@SearchActivity)
            }
            Search.apply {
                val ed = findViewByType<EditText>()!!
                findViewById<View>(R.id.search_close_btn)!!.setOnClickListener {
                    rvNews.visibility = View.GONE
                    tvNoData.visibility = View.GONE
                    ed.setText("")
                }
                isFocusable = true
                setIconifiedByDefault(false)
                requestFocus()
                val imm = getSystemService<InputMethodManager>()
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = if(query != null) {
                        rvNews.visibility = View.VISIBLE
                        vm.search(query)
                        true
                    } else false

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
            }

        }
    }

    private fun showNoData(show: Boolean = true) {
        binding.apply {
            if(show) {
                rvNews.visibility = View.GONE
                tvNoData.visibility = View.VISIBLE
            } else {
                rvNews.visibility = View.VISIBLE
                tvNoData.visibility = View.GONE
            }
        }
    }
    private fun showLoading(show: Boolean = true) {
        binding.apply {
            if(show) {
                rvNews.visibility = View.GONE
                pb.visibility = View.VISIBLE
            } else {
                rvNews.visibility = View.VISIBLE
                pb.visibility = View.GONE
            }
        }
    }
}