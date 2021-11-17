package com.example.myapplication.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myapplication.R


class SerieSqlite(contexto: Context): SerieDAO {
    companion object{
        private val BD_SERIES = "series"
        // TABELA SERIE
        private val TABELA_SERIE = "serie"
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO = "ano"
        private val COLUNA_GENERO = "genero"
        private val COLUNA_EMISSORA = "emissora"

        private val CRIAR_TABELA_SERIE_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_SERIE}(" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY, " +
                "${COLUNA_ANO} TEXT NOT NULL, " +
                "${COLUNA_GENERO} TEXT NOT NULL, " +
                "${COLUNA_EMISSORA} TEXT NOT NULL);"
    //FOREIGN KEY (nome) REFERENCES TABELA_SERIE (nome)

        // TABELA TEMPORADA

        // TABELA EPISODIO
    }
    //Referência para o banco de dados
    private val seriesBd: SQLiteDatabase =
        contexto.openOrCreateDatabase(BD_SERIES, MODE_PRIVATE, null)
    init{
         try{
             seriesBd.execSQL(CRIAR_TABELA_SERIE_STMT)
         }catch (se: SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
         }

    }


    override fun criarSerie(serie: SeriesManagerInfo): Long = seriesBd.insert(TABELA_SERIE,null, converterSerieParaContentValues(serie))


    override fun recuperarSerie(nome: String): SeriesManagerInfo {
        val serieCursor = seriesBd.query(
            true, //valores distintos
            TABELA_SERIE,
            //arrayOf(COLUNA_NOME, COLUNA_ANO, COLUNA_GENERO, COLUNA_EMISSORA)
            null, // colunas (todas)
            "${COLUNA_NOME} = ?", //where
            arrayOf(nome), // valores do where
            null,
            null,
            null,
            null
        )
        return if(serieCursor.moveToFirst()){
            with(serieCursor){
                SeriesManagerInfo(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                )
            }
        }
        else{
            SeriesManagerInfo()
        }
    }

    override fun recuperarSeries(): MutableList<SeriesManagerInfo> {
        val seriesList =  mutableListOf<SeriesManagerInfo>()

        val seriesCursor = seriesBd.rawQuery("SELECT * FROM ${TABELA_SERIE};" , null)
        while (seriesCursor.moveToNext()){
            with(seriesCursor){
                seriesList.add(
                    SeriesManagerInfo(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO)),
                        getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                        getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    )
                )
            }
        }
        return seriesList
    }

    override fun atualizarSerie(serie: SeriesManagerInfo): Int {
        val serieCv = converterSerieParaContentValues(serie)
        return seriesBd.update(TABELA_SERIE, serieCv,"${COLUNA_NOME} = ?", arrayOf(serie.nome))
    }

    override fun removerSerie(nome: String): Int = seriesBd.delete(TABELA_SERIE, "${COLUNA_NOME} = ?", arrayOf(nome))

    private fun converterSerieParaContentValues(serie: SeriesManagerInfo) = ContentValues().also{
        with(it){
            put(COLUNA_NOME, serie.nome)
            put(COLUNA_ANO, serie.ano)
            put(COLUNA_GENERO, serie.genero)
            put(COLUNA_EMISSORA, serie.emissora)
        }
    }

}