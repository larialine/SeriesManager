package aula.pdm.projetoseriesmanager.autenticacao

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

object AutenticacaoFirebase {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Opções de autenticação no Google
    var googleSignInOptions: GoogleSignInOptions? = null
    // Cliente da autenticação que interage com o WS Google
    var googleSignInClient: GoogleSignInClient? = null
    // Informações da conta autenticada
    var googleSignInAccount: GoogleSignInAccount? = null
}