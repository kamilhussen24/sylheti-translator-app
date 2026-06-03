package co.median.android.jlrnql

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import co.median.android.jlrnql.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start Lottie animation
        binding.lottieAnimation.playAnimation()

        // After 2.5 seconds, check network and navigate
        Handler(Looper.getMainLooper()).postDelayed({
            if (isNetworkAvailable()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, NoNetworkActivity::class.java))
            }
            finish()
        }, 2500)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
