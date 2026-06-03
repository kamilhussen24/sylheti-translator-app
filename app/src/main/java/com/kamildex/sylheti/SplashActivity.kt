package com.kamildex.sylheti

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
        // Logo: scale up + fade in
        val scaleX = ObjectAnimator.ofFloat(binding.splashLogo, "scaleX", 0.6f, 1.0f).apply {
            duration = 700
            interpolator = OvershootInterpolator(1.2f)
        }
        val scaleY = ObjectAnimator.ofFloat(binding.splashLogo, "scaleY", 0.6f, 1.0f).apply {
            duration = 700
            interpolator = OvershootInterpolator(1.2f)
        }
        val fadeIn = ObjectAnimator.ofFloat(binding.splashLogo, "alpha", 0f, 1f).apply {
            duration = 700
        }

        // Tagline fade in
        val taglineFade = ObjectAnimator.ofFloat(binding.splashTagline, "alpha", 0f, 1f).apply {
            duration = 500
            startDelay = 500
            interpolator = DecelerateInterpolator()
        }

        // Dots fade in then pulse
        val dotFade = ObjectAnimator.ofFloat(binding.splashDot, "alpha", 0f, 1f).apply {
            duration = 400
            startDelay = 800
        }
        val dotPulse = ObjectAnimator.ofFloat(binding.splashDot, "alpha", 1f, 0.3f).apply {
            duration = 600
            startDelay = 1400
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY, fadeIn, taglineFade, dotFade, dotPulse)
        animSet.start()

        Handler(Looper.getMainLooper()).postDelayed({
            animSet.cancel()
            val next = if (isNetworkAvailable()) MainActivity::class.java
                       else NoNetworkActivity::class.java
            startActivity(Intent(this, next))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2800)
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val net = cm.activeNetwork ?: return false
        return cm.getNetworkCapabilities(net)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
