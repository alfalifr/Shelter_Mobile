package sidev.app.bangkit.capstone.sheltermobile.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.databinding.PageWebViewBinding
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalStateException
import java.lang.Math.max

class WebViewPage: AppCompatActivity() {
    private lateinit var binding: PageWebViewBinding
    private lateinit var url: String

    /**
     * {@inheritDoc}
     *
     * Perform initialization of all fragments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = intent.getStringExtra(Const.KEY_URL) ?: throw IllegalStateException("No `Const.KEY_URL` passed to intent extras")

        binding.web.apply {
            webViewClient = object: WebViewClient() {
                private var running = 0

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val res = request?.url?.let {
                        view?.loadUrl(it.toString())
                    } != null
                    if(res) running++
                    return res
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    loge("WebView onPageFinished() url= $url")
                    if(--running == 0) {
                        showLoading(false)
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    loge("WebView onPageStarted() url= $url")
                    showLoading()
                    running = running.coerceAtLeast(1)
                }
            }
            loadUrl(this@WebViewPage.url)
        }
    }

    private fun showLoading(show: Boolean = true) {
        binding.apply {
            pb.visibility = if(show) View.VISIBLE else View.GONE
        }
    }
}