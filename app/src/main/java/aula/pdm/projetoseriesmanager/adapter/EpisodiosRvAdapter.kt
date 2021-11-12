package aula.pdm.projetoseriesmanager.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aula.pdm.projetoseriesmanager.R
import aula.pdm.projetoseriesmanager.databinding.LayoutEpisodioBinding
import aula.pdm.projetoseriesmanager.model.episodio.Episodio
import aula.pdm.projetoseriesmanager.model.episodio.onEpisodioClickListener

class EpisodiosRvAdapter(
    private val onEpisodioClickListener: onEpisodioClickListener,
    private val episodiosList: MutableList<Episodio>
): RecyclerView.Adapter<EpisodiosRvAdapter.EpisodioLayoutHolder>() {

    //Posição que será recuperada pelo menu de contexto
    var posicaoEpisodio: Int = -1

    //ViewHolder
    inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding): RecyclerView.ViewHolder(layoutEpisodioBinding.root), View.OnCreateContextMenuListener{
        val numeroTv: TextView = layoutEpisodioBinding.numeroEpisodioTv
        val nomeTv: TextView = layoutEpisodioBinding.nomeEpisodioTv
        val duracaoTv : TextView = layoutEpisodioBinding.duracaoEpisodioTv
        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_main_episodio, menu)
        }
    }


    // Quando uma nova cécula precisar ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioLayoutHolder {
        // Criar uma nova célula
        val layoutEpisodioBinding =  LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Criar um viewHolder associado a nova célula
        val viewHolder = EpisodioLayoutHolder(layoutEpisodioBinding)
        return viewHolder
    }

    // Quando necessário atualizar valores de uma cécula, seja uma célula nova ou antiga
    override fun onBindViewHolder(holder: EpisodioLayoutHolder, position: Int) {
        // Buscar episódio
        val episodio = episodiosList[position]

        // Atualizar os valores do viewHolder
        with(holder){
            numeroTv.text = episodio.numero.toString()
            nomeTv.text = episodio.nome
            duracaoTv.text = episodio.duracao.toString()
            itemView.setOnClickListener {
                onEpisodioClickListener.onEpisodioClick(position)
            }
            itemView.setOnLongClickListener{
                posicaoEpisodio = position
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return episodiosList.size
    }
}