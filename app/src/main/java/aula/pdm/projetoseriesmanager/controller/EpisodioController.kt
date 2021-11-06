package aula.pdm.projetoseriesmanager.controller

import aula.pdm.projetoseriesmanager.MainEpisodioActivity
import aula.pdm.projetoseriesmanager.model.episodio.Episodio
import aula.pdm.projetoseriesmanager.model.episodio.EpisodioDAO
import aula.pdm.projetoseriesmanager.model.episodio.EpisodioSqlite

class EpisodioController(mainEpisodioActivity: MainEpisodioActivity) {
    private val episodioDAO: EpisodioDAO = EpisodioSqlite(mainEpisodioActivity)

    fun inserirEpisodio(episodio: Episodio) = episodioDAO.criarEpisodio(episodio)
    fun buscarEpisodio(numero: Int) = episodioDAO.recuperarEpisodio(numero)
    fun buscarEpisodios() = episodioDAO.recuperarEpisodios()
    fun alterarEpisodio(episodio: Episodio) = episodioDAO.atualizarEpisodio(episodio)
    fun apagarEpisodio(numero: Int) = episodioDAO.removerEpisodio(numero)
}