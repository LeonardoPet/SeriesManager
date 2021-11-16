package com.example.myapplication.model

interface SerieDAO {
    fun criarSerie(serie: SeriesManagerInfo): Long
    fun recuperarSerie(nome: String): SeriesManagerInfo
    fun recuperarSeries(): MutableList<SeriesManagerInfo>
    fun atualizarSerie(serie: SeriesManagerInfo): Int
    fun removerSerie(nome: String): Int
}