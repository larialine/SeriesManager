package aula.pdm.projetoseriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import aula.pdm.projetoseriesmanager.adapter.SeriesRvAdapter
import aula.pdm.projetoseriesmanager.controller.SerieController
import aula.pdm.projetoseriesmanager.databinding.ActivityMainBinding
import aula.pdm.projetoseriesmanager.model.serie.Serie
import aula.pdm.projetoseriesmanager.model.serie.onSerieClickListener
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), onSerieClickListener {
    //conseguir passar parametros de uma tela para outra
    companion object Extras{
        const val EXTRA_SERIE = "EXTRA SERIE"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var serieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarSerieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var temporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    // Data source
    private val seriesList: MutableList<Serie> by lazy{
        serieController.buscarSeries()
    }

    // Controller
    private val serieController: SerieController by lazy{
        SerieController(this)
    }

    // Adapter
    private val seriesAdapter: SeriesRvAdapter by lazy{
        SeriesRvAdapter(this, seriesList)
    }

    // LayoutManager
    private val seriesLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        // Associando Adapter e LayoutManager ao RecycleView
        activityMainBinding.lista.adapter = seriesAdapter
        activityMainBinding.lista.layoutManager = seriesLayoutManager

        serieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                // recebendo serie
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    serieController.inserirSerie(this)
                    // adicionando a série no seriesList e no adapter
                    seriesList.add(this)
                    seriesAdapter.notifyDataSetChanged()
                }
            }
        }

        editarSerieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    if(posicao != null && posicao != -1){
                        serieController.alterarSerie(this)
                        seriesList[posicao] = this
                        seriesAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarHistoricoFab.setOnClickListener {
            serieActivityResultLauncher.launch(Intent(this, SerieActivity::class.java))
        }

        temporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
            if(resultado.resultCode == RESULT_OK){
                resultado.data?.getStringExtra(EXTRA_SERIE)?.let {
                    serieActivityResultLauncher.launch((Intent(this, SerieActivity::class.java)))
                }
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val posicao = seriesAdapter.posicao
        val serie = seriesList[posicao]

        return when(item.itemId){
            R.id.editarMi -> {
                //Editar a série
                val serie = seriesList[posicao]
                val editarSerieIntent = Intent(this, SerieActivity::class.java)
                editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                editarSerieActivityResultLauncher.launch(editarSerieIntent)
                true
            }
            R.id.removerMi -> {
                //Remover a série
                with(AlertDialog.Builder(this)){
                    setMessage("Deseja realmente remover item?")
                    setPositiveButton("Sim"){ _, _ ->
                        serieController.apagarSerie(serie.nome)
                        seriesList.removeAt(posicao)
                        seriesAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Item removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não"){_, _ ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()

                true
            }
            R.id.visualizarDetalhesMi -> {
                // Trocar tela para cadastro de temporadas
                val serie = seriesList[posicao]
                val exibirTelaTemporada = Intent(this, MainTemporadaActivity::class.java)
                exibirTelaTemporada.putExtra(EXTRA_SERIE, serie)
                exibirTelaTemporada.putExtra(EXTRA_POSICAO, posicao)
                temporadaActivityResultLauncher.launch(exibirTelaTemporada)
                true
            }
            else ->{
                false
            }
        }
    }

    override fun onSerieClick(posicao: Int) {
        val serie = seriesList[posicao]
        val consultarSerieIntent = Intent(this, SerieActivity::class.java)
        consultarSerieIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(consultarSerieIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.atualizarMi -> {
            seriesAdapter.notifyDataSetChanged()
            true
        }
        else -> false
    }

}