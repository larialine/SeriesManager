package aula.pdm.projetoseriesmanager.autenticacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import aula.pdm.projetoseriesmanager.MainActivity
import aula.pdm.projetoseriesmanager.databinding.ActivityAutenticacaoBinding

class AutenticacaoActivity : AppCompatActivity() {
    private val activityAutenticacaoBinding: ActivityAutenticacaoBinding by lazy {
        ActivityAutenticacaoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityAutenticacaoBinding.root)

        supportActionBar?.subtitle = "Autenticação"

        with(activityAutenticacaoBinding){
            cadastrarUsuarioBt.setOnClickListener {
                startActivity(Intent(this@AutenticacaoActivity, CadastrarUsuarioActivity::class.java))
            }

            recuperarSenhaBt.setOnClickListener {
                startActivity(Intent(this@AutenticacaoActivity, RecuperarSenhaActivity::class.java))
            }

            entrarBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                AutenticacaoFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this@AutenticacaoActivity, "Usuário autenticado com sucesso.", Toast.LENGTH_SHORT).show()
                    iniciarMainActivity()
                }.addOnFailureListener{
                    Toast.makeText(this@AutenticacaoActivity, "Usuário/senha inválido(s).", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser != null){
            iniciarMainActivity()
        }
    }

    private fun iniciarMainActivity(){
        Toast.makeText(this, "Usuário autenticado com sucesso.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@AutenticacaoActivity, MainActivity::class.java))
        finish()
    }
}