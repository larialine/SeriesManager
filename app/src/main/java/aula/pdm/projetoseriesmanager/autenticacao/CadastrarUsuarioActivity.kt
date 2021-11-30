package aula.pdm.projetoseriesmanager.autenticacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import aula.pdm.projetoseriesmanager.databinding.ActivityCadastrarUsuarioBinding

class CadastrarUsuarioActivity : AppCompatActivity() {
    private val activityCadastrarUsuarioBinding: ActivityCadastrarUsuarioBinding by lazy {
        ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityCadastrarUsuarioBinding.root)

        supportActionBar?.subtitle = "Cadastrar usuário"

        with(activityCadastrarUsuarioBinding){
            cadastrarUsuarioBt.setOnClickListener {
                val email = emailEt.text.toString()
                val senha = senhaEt.text.toString()
                val repetirSenha = repetirSenhaEt.text.toString()
                if(senha == repetirSenha){
                    //Cadastrar usuário
                    AutenticacaoFirebase.firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
                        // Usuário foi cadastrado com sucesso
                        Toast.makeText(this@CadastrarUsuarioActivity, "Usuário $email cadastrado", Toast.LENGTH_SHORT).show()
                        finish()

                    }.addOnFailureListener {
                        // Falha no cadastro do usuário
                        Toast.makeText(this@CadastrarUsuarioActivity, "Falha no cadastro", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@CadastrarUsuarioActivity, "Senhas não coincidem", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}