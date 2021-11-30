package aula.pdm.projetoseriesmanager.controller

import aula.pdm.projetoseriesmanager.MainTemporadaActivity
import aula.pdm.projetoseriesmanager.model.temporada.Temporada
import aula.pdm.projetoseriesmanager.model.temporada.TemporadaDAO
import aula.pdm.projetoseriesmanager.model.temporada.TemporadaFirebase
import aula.pdm.projetoseriesmanager.model.temporada.TemporadaSqlite

class TemporadaController(mainTemporadaActivity: MainTemporadaActivity) {
    //private val temporadaDAO: TemporadaDAO = TemporadaSqlite(mainTemporadaActivity)
    private val temporadaDAO: TemporadaDAO = TemporadaFirebase()

    fun inserirTemporada(temporada: Temporada) = temporadaDAO.criarTemporada(temporada)
    fun buscarTemporada(numero: Int) = temporadaDAO.recuperarTemporada(numero)
    fun buscarTemporadas(nome: String) = temporadaDAO.recuperarTemporadas(nome)
    fun alterarTemporada(temporada: Temporada) = temporadaDAO.atualizarTemporada(temporada)
    fun apagarTemporada(numero: Int) = temporadaDAO.removerTemporada(numero)
}