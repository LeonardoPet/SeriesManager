package com.example.myapplication.model

interface EpisodioDAO {
    fun criarEpisodio(episodio: EpisodioManagerInfo): Long
    fun recuperarEpisodio(nome: String): EpisodioManagerInfo
    fun recuperarEpisodios(): MutableList<EpisodioManagerInfo>
    fun atualizarEpisodio(episodio: EpisodioManagerInfo): Int
    fun removerEpisodio(nome: String): Int
}