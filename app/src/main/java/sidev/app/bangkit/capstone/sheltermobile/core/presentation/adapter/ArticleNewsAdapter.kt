package sidev.app.bangkit.capstone.sheltermobile.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.app.bangkit.capstone.sheltermobile.databinding.ArticleNewsListBinding
import sidev.app.bangkit.capstone.sheltermobile.ui.WebViewPage
import sidev.lib.android.std.tool.util.`fun`.startAct
import java.util.zip.Inflater

class ArticleNewsAdapter: RecyclerView.Adapter<ArticleNewsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ArticleNewsListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: News) {
            binding.apply {
                title.text = data.title
                tvGenre.text = Util.getDateStr(data.timestamp)
                description.text = data.briefDesc
                Glide.with(root.context)
                    .load(data.linkImage)
                    .into(image)
                root.setOnClickListener {
                    it.context.startAct<WebViewPage>(Const.KEY_URL to data.link)
                }
            }
        }
    }

    var dataList: List<News>? = null
        set(v){
            field = v
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ArticleNewsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataList!![position])

    override fun getItemCount(): Int = dataList?.size ?: 0
}