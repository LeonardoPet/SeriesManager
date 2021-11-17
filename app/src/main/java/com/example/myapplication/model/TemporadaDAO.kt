package com.example.myapplication.model

interface TemporadaDAO {
    fun criarTemporada(temporada: TemporadasManagerInfo): Long
    fun recuperarTemporada(nome: String): TemporadasManagerInfo
    fun recuperarTemporadas(): MutableList<TemporadasManagerInfo>
    fun atualizarTemporada(temporada: TemporadasManagerInfo): Int
    fun removerTemporada(nome: String ): Int
}