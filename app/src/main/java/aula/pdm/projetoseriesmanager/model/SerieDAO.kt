package aula.pdm.projetoseriesmanager.model

interface SerieDAO  {
    fun criarSerie(serie: Serie): Long
    fun recupararSerie(nome: String): Serie
    fun recuperarSeries(): MutableList<Serie>
    fun atualizarSerie(serie: Serie): Int       //retorna quantidade de linhas alteradas
    fun removerSerie(nome: String): Int       //retorna quantidade de linhas excluídas
}