package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityAutenticacaoBinding


class AutenticacaoActivity : AppCompatActivity() {
    private val activityAutenticacaoActivity: ActivityAutenticacaoBinding by lazy{
        ActivityAutenticacaoBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityAutenticacaoActivity.root)
        supportActionBar?.subtitle = "Autenticação"

        with(activityAutenticacaoActivity) {
            cadastrarUsuarioBt.setOnClickListener{
                startActivity(Intent(this@AutenticacaoActivity, CadastrarUsuarioActivity::class.java))
            }
            recuperarSenhaBt.setOnClickListener{
                startActivity(Intent(this@AutenticacaoActivity, RecuperarSenhaActivity::class.java))
            }
           entrarBt.setOnClickListener {
                val email = EmailEt.text.toString()
                val senha = senhaEt.text.toString()
               AutenticacaoFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                   iniciarSerieListActivity()
                   finish()
               }.addOnFailureListener {
                   Toast.makeText(this@AutenticacaoActivity, "Usuário/senha incorretos", Toast.LENGTH_SHORT).show()
               }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser != null){
            iniciarSerieListActivity()
        }
    }

    private fun iniciarSerieListActivity(){
        startActivity(Intent(this@AutenticacaoActivity, SerieListActivity::class.java))
        finish()
    }
}