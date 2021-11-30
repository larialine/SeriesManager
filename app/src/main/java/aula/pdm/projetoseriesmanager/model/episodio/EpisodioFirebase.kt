package aula.pdm.projetoseriesmanager.model.episodio

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EpisodioFirebase: EpisodioDAO {

    companion object{
        private val BD_EPISODIOS = "episodios"
    }
    // Referência para o RtDb -> Episódios
    private val episodiosRtDb = Firebase.database.getReference(BD_EPISODIOS)

    // Lista de episódios que simula uma consulta
    private val episodiosList:MutableList<Episodio> = mutableListOf()
    init{
        episodiosRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoEpisodio: Episodio? = snapshot.value as? Episodio

                novoEpisodio?.apply {
                    if(episodiosList.find{it.numero == this.numero} == null){
                        episodiosList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val episodioEditado: Episodio? = snapshot.value as? Episodio

                episodioEditado?.apply {
                    episodiosList[episodiosList.indexOfFirst { it.numero == this.numero }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val episodioRemovido: Episodio? = snapshot.value as? Episodio

                episodioRemovido?.apply {
                    episodiosList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
        episodiosRtDb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                episodiosList.clear()
                snapshot.children.forEach {
                    val episodio: Episodio = (it.getValue<Episodio>() ?: Episodio()) as Episodio
                    episodiosList.add(episodio)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    override fun criarEpisodio(episodio: Episodio): Long {
        criarOuAtualizarEpisodio(episodio)
        return 0
    }

    override fun recuperarEpisodio(numero: Int): Episodio {
        return episodiosList.firstOrNull{it.numero == numero} ?: Episodio()
    }

    override fun recuperarEpisodios(numeroTemporada: Int): MutableList<Episodio> {
        return episodiosList
    }

    override fun atualizarEpisodio(episodio: Episodio): Int {
        criarOuAtualizarEpisodio(episodio)
        return 1
    }

    override fun removerEpisodio(numero: Int): Int {
        episodiosRtDb.child(numero.toString()).removeValue()
        return 1
    }

    private fun criarOuAtualizarEpisodio(episodio: Episodio){
        episodiosRtDb.child(episodio.numero.toString()).setValue(episodio)
    }
}