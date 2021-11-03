package aula.pdm.projetoseriesmanager.controller

import aula.pdm.projetoseriesmanager.MainActivity
import aula.pdm.projetoseriesmanager.model.Serie
import aula.pdm.projetoseriesmanager.model.SerieDAO
import aula.pdm.projetoseriesmanager.model.SerieSqlite

class SerieController(mainActivity: MainActivity) {

    private val serieDAO: SerieDAO = SerieSqlite(mainActivity)

    fun inserirSerie(serie: Serie) = serieDAO.criarSerie(serie)
    fun buscarSerie(nome: String) = serieDAO.recupararSerie(nome)
    fun buscarSeries() = serieDAO.recuperarSeries()
    fun alterarSerie(serie: Serie) = serieDAO.atualizarSerie(serie)
    fun apagarSerie(nome: String) = serieDAO.removerSerie(nome)
}