package com.irjarqui.peleadoresapiaryp2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.irjarqui.peleadoresapiaryp2.R
import com.irjarqui.peleadoresapiaryp2.databinding.ActivitySplashBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Muestra imagen animada
        Glide.with(this)
            .load(R.drawable.joes_plash)
            .into(binding.splashGif)

        // Reproduce sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_intro)
        mediaPlayer?.start()

        // Espera y navega
        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
