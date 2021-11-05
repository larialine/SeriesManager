package aula.pdm.projetoseriesmanager.controller

import aula.pdm.projetoseriesmanager.MainTemporadaActivity
import aula.pdm.projetoseriesmanager.model.temporada.Temporada
import aula.pdm.projetoseriesmanager.model.temporada.TemporadaDAO
import aula.pdm.projetoseriesmanager.model.temporada.TemporadaSqlite

class TemporadaController(mainTemporadaActivity: MainTemporadaActivity) {
    private val temporadaDAO: TemporadaDAO = TemporadaSqlite(mainTemporadaActivity)

    fun inserirTemporada(temporada: Temporada) = temporadaDAO.criarTemporada(temporada)
    fun buscarTemporada(numero: Int) = temporadaDAO.recuperarTemporada(numero)
    fun buscarTemporadas() = temporadaDAO.recuperarTemporadas()
    fun alterarTemporada(temporada: Temporada) = temporadaDAO.atualizarTemporada(temporada)
    fun apagarTemporada(numero: Int) = temporadaDAO.removerTemporada(numero)
}