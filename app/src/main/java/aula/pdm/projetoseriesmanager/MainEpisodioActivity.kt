package aula.pdm.projetoseriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import aula.pdm.projetoseriesmanager.adapter.EpisodiosRvAdapter
import aula.pdm.projetoseriesmanager.autenticacao.AutenticacaoFirebase
import aula.pdm.projetoseriesmanager.controller.EpisodioController
import aula.pdm.projetoseriesmanager.databinding.ActivityMainEpisodioBinding
import aula.pdm.projetoseriesmanager.model.episodio.Episodio
import aula.pdm.projetoseriesmanager.model.episodio.onEpisodioClickListener
import com.google.android.material.snackbar.Snackbar

class MainEpisodioActivity: AppCompatActivity(), onEpisodioClickListener {

    //conseguir passar parametros de uma tela para outra
    companion object Extras{
        const val EXTRA_EPISODIO = "EXTRA EPISODIO"
        const val EXTRA_POSICAO_EPISODIO = "EXTRA_POSICAO_EPISODIO"
        const val NUMERO_TEMPORADA = "NUMERO_TEMPORADA"
    }

    private val activityMainEpisodioActivity: ActivityMainEpisodioBinding by lazy {
        ActivityMainEpisodioBinding.inflate(layoutInflater)
    }

    private lateinit var episodioActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>

    // Data source
    private val episodiosList: MutableList<Episodio> by lazy {
        episodioController.buscarEpisodios(Integer.parseInt(activityMainEpisodioActivity.temporadaTv.text.toString()))
    }

    //Controller
    private val episodioController: EpisodioController by lazy {
        EpisodioController(this)
    }

    // Adapter
    private val episodiosAdapter: EpisodiosRvAdapter by lazy{
        EpisodiosRvAdapter(this, episodiosList)
    }

    // LayoutManager
    private val episodioLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainEpisodioActivity.root)

        supportActionBar?.title = "Epis??dios"

        intent.getStringExtra(MainTemporadaActivity.NUMERO_TEMPORADA)?.run {
            activityMainEpisodioActivity.temporadaTv.text = this
        }

        // Associando Adapter e LayoutManager ao RecycleView
        activityMainEpisodioActivity.episodiosRv.adapter = episodiosAdapter
        activityMainEpisodioActivity.episodiosRv.layoutManager = episodioLayoutManager


        episodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado ->
            if (resultado.resultCode == RESULT_OK){
                //recebendo epis??dio
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    episodioController.inserirEpisodio(this)
                    //adicionando epis??dio no episodiosList e no Adapter
                    episodiosList.add(this)
                    episodiosAdapter.notifyDataSetChanged()

                }
            }
        }

        editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado ->
            if (resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO_EPISODIO, -1)
                resultado.data?.getParcelableExtra<Episodio>(EXTRA_EPISODIO)?.apply {
                    if(posicao != null && posicao != -1){
                        episodioController.alterarEpisodio(this)
                        episodiosList[posicao] = this
                        episodiosAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainEpisodioActivity.adicionarEpisodioFab.setOnClickListener{
            val cadastroEpisodioActivityIntent = Intent (this, EpisodioActivity::class.java)
            cadastroEpisodioActivityIntent.putExtra(NUMERO_TEMPORADA, activityMainEpisodioActivity.temporadaTv.text)
            episodioActivityResultLauncher.launch(cadastroEpisodioActivityIntent)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = episodiosAdapter.posicaoEpisodio
        val episodio = episodiosList[posicao]
        val numeroTemporada = episodio.temporada

        return when (item.itemId) {
            R.id.editarMi -> {
                //Editar epis??dio
                val episodio = episodiosList[posicao]
                val editarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
                editarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(EXTRA_POSICAO_EPISODIO, posicao)
                editarEpisodioIntent.putExtra(NUMERO_TEMPORADA, numeroTemporada)
                editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)
                true
            }
            R.id.removerMi -> {
                //Remover epis??dio
                with(AlertDialog.Builder(this)){
                    setMessage("Confirma remo????o?")
                    setPositiveButton("Sim"){ _, _ ->
                        episodioController.apagarEpisodio(episodio.numero)
                        episodiosList.removeAt(posicao)
                        episodiosAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainEpisodioActivity.root, "Item removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("N??o"){_, _ ->
                        Snackbar.make(activityMainEpisodioActivity.root, "Remo????o cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            }

            else -> {
                false
            }
        }
    }

    override fun onEpisodioClick(posicao: Int) {
        val episodio  = episodiosList[posicao]
        val consultarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
        consultarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
        startActivity(consultarEpisodioIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.sairMi -> {
            AutenticacaoFirebase.firebaseAuth.signOut()
            finish()
            true
        }
        else -> false
    }

    override fun onStart() {
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser == null){
            finish()
        }
    }
}