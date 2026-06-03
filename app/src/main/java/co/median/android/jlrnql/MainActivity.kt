package co.median.android.jlrnql

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import co.median.android.jlrnql.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val webUrl = "https://sylheti.kamildex.com/"

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isNetworkAvailable()) {
                startActivity(Intent(this@MainActivity, NoNetworkActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()
        handleDeepLink(intent)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
                userAgentString = "MyAppWebView"
                loadWithOverviewMode = true
                useWideViewPort = true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return when {
                        url.startsWith("app://share") -> {
                            shareApp()
                            true
                        }
                        url.startsWith("app://openurl") -> {
                            val target = Uri.parse(url).getQueryParameter("url") ?: return false
                            openCustomTab(target)
                            true
                        }
                        url.startsWith("https://sylheti.kamildex.com") -> {
                            false
                        }
                        url.startsWith("http") -> {
                            openCustomTab(url)
                            true
                        }
                        else -> false
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.show()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.hide()

                    // window.AppInventor inject — website এর সাথে কাজ করবে
                    view?.evaluateJavascript("""
                        (function() {
                            window.AppInventor = {
                                setWebViewString: function(value) {
                                    if (value === 'share') {
                                        window.location.href = 'app://share';
                                    } else {
                                        window.location.href = 'app://openurl?url=' + encodeURIComponent(value);
                                    }
                                }
                            };
                        })();
                    """.trimIndent(), null)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    binding.progressBar.progress = newProgress
                }
            }

            loadUrl(webUrl)
        }
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            val url = uri.toString()
            if (url.contains("sylheti.kamildex.com")) {
                binding.webView.loadUrl(url)
            }
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "দেখে আসুন নতুন সিলেটি ট্রান্সলেটর\nhttps://play.google.com/store/apps/details?id=co.median.android.jlrnql")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun openCustomTab(url: String) {
        try {
            val colorSchemeParams = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                .build()

            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(colorSchemeParams)
                .setShowTitle(true)
                .build()
                .launchUrl(this, Uri.parse(url))
        } catch (e: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            moveTaskToBack(true)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(networkReceiver) } catch (e: Exception) {}
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
