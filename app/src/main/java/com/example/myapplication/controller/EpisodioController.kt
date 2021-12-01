package com.example.myapplication.controller

import com.example.myapplication.EpisodioActivity
import com.example.myapplication.model.EpisodioDAO
import com.example.myapplication.model.EpisodioFirebase
import com.example.myapplication.model.EpisodioManagerInfo
import com.example.myapplication.model.EpisodioSqlite

class EpisodioController (EpisodioActivity: EpisodioActivity) {
    private val episodioDAO: EpisodioDAO = EpisodioFirebase()

    fun inserirEpisodio (episodio: EpisodioManagerInfo) = episodioDAO.criarEpisodio(episodio)
    fun buscarEpisodio (nome: String) =  episodioDAO.recuperarEpisodio(nome)
    fun buscarEpisodios () = episodioDAO.recuperarEpisodios()
    fun modificarEpisodio (episodio: EpisodioManagerInfo) = episodioDAO.atualizarEpisodio(episodio)
    fun apagarEpisodio (nome: String) = episodioDAO.removerEpisodio(nome)
}