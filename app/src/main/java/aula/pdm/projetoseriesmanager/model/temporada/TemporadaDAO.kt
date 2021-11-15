package aula.pdm.projetoseriesmanager.model.temporada

interface TemporadaDAO {
    fun criarTemporada(temporada: Temporada): Long
    fun recuperarTemporada(numero: Int): Temporada
    fun recuperarTemporadas(nome: String): MutableList<Temporada>
    fun atualizarTemporada(temporada: Temporada): Int     //retorna quantidade de linhas alteradas
    fun removerTemporada(numero: Int): Int                //retorna quantidade de linhas excluídas
}
