package com.example.myapplication.controller

import com.example.myapplication.SerieListActivity
import com.example.myapplication.model.SerieDAO
import com.example.myapplication.model.SerieSqlite
import com.example.myapplication.model.SeriesManagerInfo

class SerieController (SerieListActivity: SerieListActivity) {
    private val serieDAO: SerieDAO = SerieSqlite(SerieListActivity)

    fun inserirSerie(serie: SeriesManagerInfo) = serieDAO.criarSerie(serie)
    fun buscarSerie(nome: String) = serieDAO.recuperarSerie(nome)
    fun buscarSeries() = serieDAO.recuperarSeries()
    fun modificarSerie(serie: SeriesManagerInfo) = serieDAO.atualizarSerie(serie)
    fun apagarSerie(nome: String) = serieDAO.recuperarSerie(nome)
}