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
    private val shareMessage = "দেখে আসুন নতুন সিলেটি ট্রান্সলেটর\nhttps://play.google.com/store/apps/details?id=co.median.android.jlrnql"

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

        // Register network disconnect listener
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
                    return if (url.startsWith("https://sylheti.kamildex.com")) {
                        false // Load inside WebView
                    } else {
                        openCustomTab(url)
                        true
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.show()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.hide()

                    // Inject JavaScript bridge for share functionality
                    view?.evaluateJavascript("""
                        (function() {
                            window.AndroidBridge = {
                                share: function() {
                                    window.location.href = 'app://share';
                                },
                                openUrl: function(url) {
                                    window.location.href = 'app://openurl?url=' + encodeURIComponent(url);
                                }
                            };
                        })();
                    """.trimIndent(), null)
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.progressBar.progress = newProgress
                }
            }

            // Handle app:// scheme for share/openurl from web
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return when {
                        url == "app://share" || url.contains("share") && !url.contains("http") -> {
                            shareApp()
                            true
                        }
                        url.startsWith("app://openurl") -> {
                            val target = Uri.parse(url).getQueryParameter("url") ?: return false
                            openCustomTab(target)
                            true
                        }
                        url.startsWith("https://sylheti.kamildex.com") -> {
                            false // Stay in WebView
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
            putExtra(Intent.EXTRA_TEXT, shareMessage)
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
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
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
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
            // Already unregistered
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
