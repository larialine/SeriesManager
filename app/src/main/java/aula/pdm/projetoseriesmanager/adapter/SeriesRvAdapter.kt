package aula.pdm.projetoseriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aula.pdm.projetoseriesmanager.R
import aula.pdm.projetoseriesmanager.databinding.LayoutSerieBinding
import aula.pdm.projetoseriesmanager.model.serie.Serie
import aula.pdm.projetoseriesmanager.model.serie.onSerieClickListener

class SeriesRvAdapter(
    private val onSerieClickListener: onSerieClickListener,
    private val seriesList: MutableList<Serie>
): RecyclerView.Adapter<SeriesRvAdapter.SerieLayoutHolder>() {

    //Posição que será recuperada pelo menu de contexto
    var posicaoSerie: Int = -1

    //ViewHolder
    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding): RecyclerView.ViewHolder(layoutSerieBinding.root), View.OnCreateContextMenuListener{
        val nomeTv: TextView = layoutSerieBinding.nomeSerieTv
        val anoTv: TextView = layoutSerieBinding.anoSerieTv
        val emissoraTv: TextView = layoutSerieBinding.emissoraSerieTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main, menu)
        }
    }


    // Quando uma nova cécular precisar ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        // Criar uma nova célula
        val layoutSerieBinding =  LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um viewHolder associado a nova célula
        val viewHolder: SerieLayoutHolder = SerieLayoutHolder(layoutSerieBinding)
        return viewHolder
    }

    // Quando necessário atualizar valores de uma cécula, seja uma célula nova ou antiga
    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
        // Buscar série
        val serie = seriesList[position]

        // Atualizar os valores do viewHolder
        with(holder){
            nomeTv.text = serie.nome
            anoTv.text = serie.ano.toString()
            emissoraTv.text = serie.emissora
            itemView.setOnClickListener {
                onSerieClickListener.onSerieClick(position)
            }
            itemView.setOnLongClickListener{
                posicaoSerie = position
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }
}