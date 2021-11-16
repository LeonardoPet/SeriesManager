package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.myapplication.databinding.ActivitySerieBinding


class SerieActivity : AppCompatActivity() {
    private val aptvb: ActivitySerieBinding by lazy{
        ActivitySerieBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aptvb.root)

        aptvb.btnAssistir.setOnClickListener {
           iniciarSegundaTela()
        }

    }

    private fun iniciarSegundaTela() {
        val segundaTela = Intent(this, SerieListActivity::class.java)
       startActivity(segundaTela)
    }


}