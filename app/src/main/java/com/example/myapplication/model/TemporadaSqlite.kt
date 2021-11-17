package com.example.myapplication.model

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.R

class TemporadaSqlite(contexto: Context): TemporadaDAO {
    companion object {
        private val BD_TEMPORADAS = "temporadas"

        // TABELA TEMPORADA
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO = "ano"
        private val COLUNA_QTDEPISODIO = "qtdEpisodio"

        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_TEMPORADA}(" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUNA_ANO} TEXT NOT NULL, " +
                "${COLUNA_QTDEPISODIO} TEXT NOT NULL, " +
                "FOREIGN KEY (nome) REFERENCES TABELA_SERIE (nome);"
    }

    //Referência para o banco de dados
    private val temporadasBd: SQLiteDatabase =
        contexto.openOrCreateDatabase(BD_TEMPORADAS, Context.MODE_PRIVATE, null)
    init{
        try{
            temporadasBd.execSQL(CRIAR_TABELA_TEMPORADA_STMT)
        }catch (se: SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }

    }

    override fun criarTemporada(temporada: TemporadasManagerInfo): Long = temporadasBd.insert(TABELA_TEMPORADA,null,converterTemporadaParaContentValues(temporada))

    override fun recuperarTemporada(nome: String): TemporadasManagerInfo {
        val temporadaCursor = temporadasBd.query(
            true,
            TABELA_TEMPORADA,
            null,
            "${COLUNA_NOME} = ?",
            arrayOf(nome),
            null,
            null,
            null,
            null
        )
        return if(temporadaCursor.moveToFirst()){
            with(temporadaCursor){
                TemporadasManagerInfo(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO)),
                    getString(getColumnIndexOrThrow(COLUNA_QTDEPISODIO)),
                )
            }
        }
        else{
            TemporadasManagerInfo()
        }
    }

    override fun recuperarTemporadas(): MutableList<TemporadasManagerInfo> {
        val temporadasList =  mutableListOf<TemporadasManagerInfo>()

        val temporadaCursor = temporadasBd.rawQuery("SELECT * FROM ${TABELA_TEMPORADA};" , null)
        while (temporadaCursor.moveToNext()){
            with(temporadaCursor){
                temporadasList.add(
                    TemporadasManagerInfo(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO)),
                        getString(getColumnIndexOrThrow(COLUNA_QTDEPISODIO)),
                    )
                )
            }
        }
        return temporadasList
    }


    override fun atualizarTemporada(temporada: TemporadasManagerInfo): Int {
        val TemporadaCv = converterTemporadaParaContentValues(temporada)
        return temporadasBd.update(TABELA_TEMPORADA, TemporadaCv,"${COLUNA_NOME} = ?", arrayOf(temporada.nome))
    }

    override fun removerTemporada(nome: String): Int = temporadasBd.delete(TABELA_TEMPORADA, "${COLUNA_NOME} = ?", arrayOf(nome))

    private fun converterTemporadaParaContentValues(temporada: TemporadasManagerInfo) = ContentValues().also{
        with(it){
            put(COLUNA_NOME,temporada.nome)
            put(COLUNA_ANO, temporada.ano)
            put(COLUNA_QTDEPISODIO, temporada.qtdEpisodio)
        }
    }
}
