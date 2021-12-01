package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityCadastrarUsuarioBinding

class CadastrarUsuarioActivity : AppCompatActivity() {
    private val activityCadastrarUsuarioBinding: ActivityCadastrarUsuarioBinding by lazy{
        ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCadastrarUsuarioBinding.root)
        supportActionBar?.subtitle = "Cadastrar usuário"
        with(activityCadastrarUsuarioBinding){
            cadastrarUsuarioBt.setOnClickListener{
                val email = EmailEt.text.toString()
                val senha = senhaEt.text.toString()
                val repetirSenha = repetirSenhaEt.text.toString()
                if(senha == repetirSenha){
                    // Cadastrar usuario
                    AutenticacaoFirebase.firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
                        // Usuário foi cadastrado com sucesso
                        Toast.makeText(this@CadastrarUsuarioActivity, "Usuário $email cadastrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener{
                        // Falha no Cadastro
                        Toast.makeText(this@CadastrarUsuarioActivity, "Falha no Cadastro", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@CadastrarUsuarioActivity, "Senhas não são iguais", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}