package co.median.android.jlrnql

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.median.android.jlrnql.databinding.ActivityNoNetworkBinding

class NoNetworkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoNetworkBinding

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable()) {
                // Network is back — go to Main
                startActivity(Intent(this@NoNetworkActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start no-network Lottie animation
        binding.lottieAnimation.playAnimation()

        // Listen for network reconnect
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onBackPressed() {
        // Close app on back press
        finishAffinity()
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
