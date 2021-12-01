package com.example.myapplication.controller

import com.example.myapplication.TemporadaActivity
import com.example.myapplication.model.TemporadaDAO
import com.example.myapplication.model.TemporadaFirebase
import com.example.myapplication.model.TemporadaSqlite
import com.example.myapplication.model.TemporadasManagerInfo

class TemporadaController (TemporadaActivity : TemporadaActivity) {
    private val temporadaDAO: TemporadaDAO = TemporadaFirebase()

    fun inserirTemporada (temporada: TemporadasManagerInfo) = temporadaDAO.criarTemporada(temporada)
    fun buscarTemporada(nome: String) = temporadaDAO.recuperarTemporada(nome)
    fun buscarTemporadas() = temporadaDAO.recuperarTemporadas()
    fun modificarTemporada(temporada: TemporadasManagerInfo) = temporadaDAO.atualizarTemporada(temporada)
    fun apagarTemporada (nome: String) = temporadaDAO.removerTemporada(nome)

}