package aula.pdm.projetoseriesmanager.model.temporada

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TemporadaFirebase: TemporadaDAO {

    companion object{
        private val BD_TEMPORADAS = "temporadas"
    }
    // Referência para o RtDb -> Temporadas
    private val temporadasRtDb = Firebase.database.getReference(BD_TEMPORADAS)

    // Lista de temporadas que simula uma consulta
    private val temporadasList:MutableList<Temporada> = mutableListOf()
    init{
        temporadasRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaTemporada: Temporada? = snapshot.value as? Temporada

                novaTemporada?.apply {
                    if(temporadasList.find{it.numero == this.numero} == null){
                        temporadasList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val temporadaEditada: Temporada? = snapshot.value as? Temporada

                temporadaEditada?.apply {
                    temporadasList[temporadasList.indexOfFirst { it.numero == this.numero }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val temporadaRemovida: Temporada? = snapshot.value as? Temporada

                temporadaRemovida?.apply {
                    temporadasList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
        temporadasRtDb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                temporadasList.clear()
                snapshot.children.forEach {
                    val temporada: Temporada = (it.getValue<Temporada>() ?: Temporada()) as Temporada
                    temporadasList.add(temporada)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    override fun criarTemporada(temporada: Temporada): Long {
        criarOuAtualizarTemporada(temporada)
        return 0
    }

    override fun recuperarTemporada(numero: Int): Temporada {
        return temporadasList.firstOrNull{it.numero == numero} ?: Temporada()
    }

    override fun recuperarTemporadas(nome: String): MutableList<Temporada> {
        return temporadasList
    }

    override fun atualizarTemporada(temporada: Temporada): Int {
        criarOuAtualizarTemporada(temporada)
        return 1
    }

    override fun removerTemporada(numero: Int): Int {
        temporadasRtDb.child(numero.toString()).removeValue()
        return 1
    }

    private fun criarOuAtualizarTemporada(temporada: Temporada){
        temporadasRtDb.child(temporada.numero.toString()).setValue(temporada)
    }
}