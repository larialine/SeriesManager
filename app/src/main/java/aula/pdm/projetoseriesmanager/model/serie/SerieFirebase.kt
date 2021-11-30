package aula.pdm.projetoseriesmanager.model.serie

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SerieFirebase: SerieDAO {

    companion object{
        private val BD_SERIES = "series"
    }
    // Referência para o RtDb -> Series
    private val seriesRtDb = Firebase.database.getReference(BD_SERIES)

    // Lista de séries que simula uma consulta
    private val seriesList:MutableList<Serie> = mutableListOf()
    init{
        seriesRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaSerie: Serie? = snapshot.value as? Serie

                novaSerie?.apply {
                    if(seriesList.find{it.nome == this.nome} == null){
                        seriesList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val serieEditada: Serie? = snapshot.value as? Serie

                serieEditada?.apply {
                    seriesList[seriesList.indexOfFirst { it.nome == this.nome }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val serieRemovida: Serie? = snapshot.value as? Serie

                serieRemovida?.apply {
                    seriesList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
        seriesRtDb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                seriesList.clear()
                snapshot.children.forEach {
                    val serie: Serie = (it.getValue<Serie>() ?: Serie()) as Serie
                    seriesList.add(serie)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    override fun criarSerie(serie: Serie): Long {
        criarOuAtualizarSerie(serie)
        return 0
    }

    override fun recupararSerie(nome: String): Serie {
        return seriesList.firstOrNull{it.nome == nome} ?: Serie()
    }

    override fun recuperarSeries(): MutableList<Serie> {
        return seriesList
    }

    override fun atualizarSerie(serie: Serie): Int {
        criarOuAtualizarSerie(serie)
        return 1
    }

    override fun removerSerie(nome: String): Int {
        seriesRtDb.child(nome).removeValue()
        return 1
    }

    private fun criarOuAtualizarSerie(serie: Serie){
        seriesRtDb.child(serie.nome).setValue(serie)
    }
}