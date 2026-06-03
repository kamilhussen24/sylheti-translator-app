package co.median.android.jlrnql

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import co.median.android.jlrnql.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplashAnimation()
    }

    private fun startSplashAnimation() {

        // Logo: scale from 0.6 to 1.0 + fade in
        val logoScaleX = ObjectAnimator.ofFloat(binding.splashLogo, "scaleX", 0.6f, 1.0f)
        val logoScaleY = ObjectAnimator.ofFloat(binding.splashLogo, "scaleY", 0.6f, 1.0f)
        val logoFade = ObjectAnimator.ofFloat(binding.splashLogo, "alpha", 0f, 1f)
        logoScaleX.duration = 700
        logoScaleY.duration = 700
        logoFade.duration = 700
        logoScaleX.interpolator = OvershootInterpolator(1.2f)
        logoScaleY.interpolator = OvershootInterpolator(1.2f)

        val logoAnim = AnimatorSet()
        logoAnim.playTogether(logoScaleX, logoScaleY, logoFade)

        // Tagline: fade in after logo
        val taglineFade = ObjectAnimator.ofFloat(binding.splashTagline, "alpha", 0f, 1f)
        taglineFade.duration = 500
        taglineFade.startDelay = 500
        taglineFade.interpolator = DecelerateInterpolator()

        // Dots: fade in last
        val dotFade = ObjectAnimator.ofFloat(binding.splashDot, "alpha", 0f, 1f)
        dotFade.duration = 400
        dotFade.startDelay = 800
        dotFade.interpolator = DecelerateInterpolator()

        // Dots pulse animation
        val dotPulse = ObjectAnimator.ofFloat(binding.splashDot, "alpha", 1f, 0.3f)
        dotPulse.duration = 600
        dotPulse.startDelay = 1400
        dotPulse.repeatCount = ObjectAnimator.INFINITE
        dotPulse.repeatMode = ObjectAnimator.REVERSE

        // Play all together
        val fullAnim = AnimatorSet()
        fullAnim.playTogether(logoAnim, taglineFade, dotFade, dotPulse)
        fullAnim.start()

        // Navigate after 2.8 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            fullAnim.cancel()
            navigateNext()
        }, 2800)
    }

    private fun navigateNext() {
        val intent = if (isNetworkAvailable()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, NoNetworkActivity::class.java)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
