package sidev.app.bangkit.capstone.sheltermobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.ArticleNewsAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.SearchArticleViewModel
import sidev.app.bangkit.capstone.sheltermobile.databinding.ActivitySearchBinding

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
            }
        }

        binding.apply {
            rvNews.apply {
                adapter = adp
                layoutManager = LinearLayoutManager(this@SearchActivity)
            }
            Search.apply {
                requestFocus()
                val imm = getSystemService<InputMethodManager>()
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = if(query != null) {
                        vm.search(query)
                        true
                    } else false

                    override fun onQueryTextChange(newText: String?): Boolean = false
                })
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