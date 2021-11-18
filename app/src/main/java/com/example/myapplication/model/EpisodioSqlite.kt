package com.example.myapplication.model

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.R

class EpisodioSqlite(contexto: Context): EpisodioDAO{
    companion object {
        private val BD_EPISODIOS = "episodios"

        // TABELA EPISODIO
        private val TABELA_EPISODIO ="episodio"
        private val COLUNA_NOME = "nome"
        private val COLUNA_NOME_EPISODIO = "nomeEpisodio"
        private val COLUNA_TEMPO_DURACAO = "tempoDuracao"
        private val COLUNA_NUM_SEQUENCIAL = "numSequencial"

        private val CRIAR_TABELA_EPISODIO_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_EPISODIO}(" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUNA_NUM_SEQUENCIAL} INT, " +
                "${COLUNA_NOME_EPISODIO} TEXT NOT NULL, " +
                "${COLUNA_TEMPO_DURACAO} TEXT NOT NULL, " +
                "FOREIGN KEY (nome) REFERENCES TABELA_SERIE (nome));"
    }

    //Referência para o banco de dados
    private val episodiosBd: SQLiteDatabase =
        contexto.openOrCreateDatabase(BD_EPISODIOS, Context.MODE_PRIVATE, null)
    init{
        try{
            episodiosBd.execSQL(CRIAR_TABELA_EPISODIO_STMT)
        }catch (se: SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }

    }

    override fun criarEpisodio(episodio: EpisodioManagerInfo): Long = episodiosBd.insert(TABELA_EPISODIO,null, converterEpisodioParaContentValues(episodio))

    override fun recuperarEpisodio(nome: String): EpisodioManagerInfo {
        val episodioCursor = episodiosBd.query(
            true,
            TABELA_EPISODIO,
            null,
            "${COLUNA_NOME} = ?",
            arrayOf(nome),
            null,
            null,
            null,
            null
        )
        return if(episodioCursor.moveToFirst()){
            with(episodioCursor){
                EpisodioManagerInfo(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_NUM_SEQUENCIAL)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME_EPISODIO)),
                    getString(getColumnIndexOrThrow(COLUNA_TEMPO_DURACAO))

                )
            }
        }
        else{
            EpisodioManagerInfo()
        }
    }

    override fun recuperarEpisodios(): MutableList<EpisodioManagerInfo> {
        val episodiosList =  mutableListOf<EpisodioManagerInfo>()

        val episodioCursor = episodiosBd.rawQuery("SELECT * FROM ${TABELA_EPISODIO};" , null)
        while (episodioCursor.moveToNext()){
            with(episodioCursor){
                episodiosList.add(
                   EpisodioManagerInfo(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getString(getColumnIndexOrThrow(COLUNA_NUM_SEQUENCIAL)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_EPISODIO)),
                        getString(getColumnIndexOrThrow(COLUNA_TEMPO_DURACAO))
                    )
                )
            }
        }
        return episodiosList
    }


    override fun atualizarEpisodio(episodio: EpisodioManagerInfo): Int {
        val EpisodioCv = converterEpisodioParaContentValues(episodio)
        return episodiosBd.update(TABELA_EPISODIO, EpisodioCv,"${EpisodioSqlite.COLUNA_NOME} = ?", arrayOf(episodio.nome))
    }

    override fun removerEpisodio(nome: String): Int = episodiosBd.delete(TABELA_EPISODIO, "${COLUNA_NOME} = ?", arrayOf(nome))

    private fun converterEpisodioParaContentValues(episodio: EpisodioManagerInfo) = ContentValues().also{
        with(it){
            put(COLUNA_NOME,episodio.nome)
            put(COLUNA_NOME_EPISODIO,episodio.nomeEpisodio)
            put(COLUNA_TEMPO_DURACAO, episodio.tempoDuracao)
            put(COLUNA_NUM_SEQUENCIAL,episodio.numSequencialEp)
        }
    }

}
