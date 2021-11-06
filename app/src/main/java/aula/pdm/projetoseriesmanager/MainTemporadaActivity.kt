package aula.pdm.projetoseriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import aula.pdm.projetoseriesmanager.adapter.TemporadasRvAdapter
import aula.pdm.projetoseriesmanager.controller.TemporadaController
import aula.pdm.projetoseriesmanager.databinding.ActivityMainBinding
import aula.pdm.projetoseriesmanager.model.temporada.Temporada
import aula.pdm.projetoseriesmanager.model.temporada.onTemporadaClickListener
import com.google.android.material.snackbar.Snackbar

class MainTemporadaActivity: AppCompatActivity(), onTemporadaClickListener {

    //conseguir passar parametros de uma tela para outra
    companion object Extras{
        const val EXTRA_EPISODIO = "EXTRA TEMPORADA"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var temporadaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    // Data source
    private val temporadasList: MutableList<Temporada> by lazy {
        temporadaController.buscarTemporadas()
    }

    //Controller
    private val temporadaController: TemporadaController by lazy {
        TemporadaController(this)
    }

    // Adapter
    private val temporadaAdapter: TemporadasRvAdapter by lazy{
        TemporadasRvAdapter(this, temporadasList)
    }

    // LayoutManager
    private val temporadaLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        // Associando Adapter e LayoutManager ao RecycleView
        activityMainBinding.lista.adapter = temporadaAdapter
        activityMainBinding.lista.layoutManager = temporadaLayoutManager


        temporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado ->
            if (resultado.resultCode == RESULT_OK){
                //recebendo a temporada
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_EPISODIO)?.apply {
                    temporadaController.inserirTemporada(this)
                    //adicionando temporada no temporadasList e no Adapter
                    temporadasList.add(this)
                    temporadaAdapter.notifyDataSetChanged()

                }
            }
        }

        editarTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado ->
            if (resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Temporada>(EXTRA_EPISODIO)?.apply {
                    if(posicao != null && posicao != -1){
                        temporadaController.alterarTemporada(this)
                        temporadasList[posicao] = this
                        temporadaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarHistoricoFab.setOnClickListener{
            temporadaActivityResultLauncher.launch(Intent(this, TemporadaActivity::class.java))
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao
        val temporada = temporadasList[posicao]

        return when (item.itemId) {
            R.id.editarMi -> {
                //Editar temporada
                val temporada = temporadasList[posicao]
                val editarTemporadaIntent = Intent(this, TemporadaActivity::class.java)
                editarTemporadaIntent.putExtra(EXTRA_EPISODIO, temporada)
                editarTemporadaIntent.putExtra(EXTRA_POSICAO, posicao)
                editarTemporadaActivityResultLauncher.launch(editarTemporadaIntent)
                true
            }
            R.id.removerMi -> {
                //Remover temporada
                with(AlertDialog.Builder(this)){
                    setMessage("Confirma remoção?")
                    setPositiveButton("Sim"){ _, _ ->
                        temporadaController.apagarTemporada(temporada.numero)
                        temporadasList.removeAt(posicao)
                        temporadaAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Item removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não"){_, _ ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
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

    override fun onTemporadaClick(posicao: Int) {
        val temporada  = temporadasList[posicao]
        val consultarTemporadaIntent = Intent(this, TemporadaActivity::class.java)
        consultarTemporadaIntent.putExtra(EXTRA_EPISODIO, temporada)
        startActivity(consultarTemporadaIntent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.atualizarMi -> {
            temporadaAdapter.notifyDataSetChanged()
            true
        }
        else -> false
    }
}