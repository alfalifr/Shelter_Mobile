package sidev.app.bangkit.capstone.sheltermobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sidev.app.bangkit.capstone.sheltermobile.core.di.ViewModelDi
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter.ArticleNewsAdapter
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.viewmodel.NewsViewModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.databinding.ArticleNewsListBinding
import sidev.app.bangkit.capstone.sheltermobile.databinding.FragmentArticleNewsBinding
import sidev.lib.android.std.tool.util.`fun`.startAct


class ArticleNewsFragment:  Fragment() {
    private lateinit var binding: FragmentArticleNewsBinding
    private lateinit var vm: NewsViewModel
    private lateinit var newsAdp: ArticleNewsAdapter
    private lateinit var articleAdp: ArticleNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelDi.getNewsViewModel(this)
        newsAdp = ArticleNewsAdapter()
        articleAdp = ArticleNewsAdapter()

        binding.apply {
            rvArticle.apply {
                adapter = articleAdp
                layoutManager = LinearLayoutManager(requireContext())
            }
            rvNews.apply {
                adapter = newsAdp
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            }
            Search.apply {
                isEnabled = false
                setOnClickListener {
                    startAct<SearchActivity>()
                }
            }
        }

        vm.apply {
            onPreAsyncTask {
                binding.apply {
                    when(it) {
                        Const.KEY_ARTICLE_LIST -> showLoading(pbArticle, rvArticle)
                        Const.KEY_NEWS_LIST -> showLoading(pbNews, rvNews)
                    }
                }
            }
            newsList.observe(viewLifecycleOwner) {
                newsAdp.dataList = it
                showLoading(binding.pbNews, binding.rvNews, it == null)
            }
            articleList.observe(viewLifecycleOwner) {
                articleAdp.dataList = it
                showLoading(binding.pbArticle, binding.rvArticle, it == null)
            }
            getArticleList()
            getNewsList()
        }
    }


    private fun showNoData(pb: ProgressBar, loadedView: View, show: Boolean = true) {}
    private fun showLoading(pb: ProgressBar, loadedView: View, show: Boolean = true) {
        if(show){
            pb.visibility = View.VISIBLE
            loadedView.visibility = View.GONE
        } else {
            pb.visibility = View.GONE
            loadedView.visibility = View.VISIBLE
        }
    }
}


/*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sidev.app.bangkit.capstone.sheltermobile.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleNewsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_article_news, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticleNewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArticleNewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

 */