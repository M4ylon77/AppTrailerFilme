package com.maxpayneman.busca_trailler.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.maxpayneman.busca_trailler.R
import com.maxpayneman.busca_trailler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val videoView: VideoView = binding.video

        // Caminho ou URL do vídeo que você deseja reproduzir
        val videoPath = "android.resource://" + packageName + "/" + R.raw.loads
        // Define a fonte do vídeo no VideoView
        videoView.setVideoURI(Uri.parse(videoPath))
        // Inicia a reprodução do vídeo
        videoView.start()

        var valor = 0

        Thread {
            do{
                runOnUiThread{
                if (valor >= 0 && valor <= 9 ) {
                    binding.`val`.text = "CineXplore apresenta..."
                }
                if (valor == 10) {

                    binding.`val`.text = "[BuscaXtrailer]"
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(applicationContext, BuscarActivity::class.java))
                        finish()
                    },1500)

                }
            }
            valor++
            Thread.sleep(350)
        }while(valor != 10)
        }.start()
    }
}


