package com.example.myapplication.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SerieFirebase: SerieDAO {
    companion object {
        private val BD_SERIES = "series"
    }
    // referência ao Firebase
    private  val seriesFb = Firebase.database.getReference(BD_SERIES)

    // Lista de series que faz consulta
    private  val seriesList: MutableList<SeriesManagerInfo> = mutableListOf()
    init {
        seriesFb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoSerie: SeriesManagerInfo? = snapshot.value as? SeriesManagerInfo
                    novoSerie?.apply {
                    if(seriesList.find {it.nome == novoSerie.nome} == null){
                        seriesList.add(novoSerie)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val serieEditado: SeriesManagerInfo? = snapshot.value as? SeriesManagerInfo
                serieEditado?.apply {
                    seriesList[seriesList.indexOfFirst( {it.nome == this.nome})] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val serieRemovido: SeriesManagerInfo? = snapshot.value as? SeriesManagerInfo
                serieRemovido?.apply {
                    seriesList.remove(this)

                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
               // return nada
            }

            override fun onCancelled(error: DatabaseError) {
                // return nada
            }
        })

        seriesFb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                seriesList.clear()
                snapshot.children.forEach {
                    val serie: SeriesManagerInfo = it.getValue<SeriesManagerInfo>()?: SeriesManagerInfo()
                    seriesList.add(serie)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // return nada
            }
        })
    }

    override fun criarSerie(serie: SeriesManagerInfo): Long {
        criarOuAtualizarSerie(serie)
        return 0L
    }

    override fun recuperarSerie(nome: String): SeriesManagerInfo = seriesList.firstOrNull{it.nome == nome} ?: SeriesManagerInfo()


    override fun recuperarSeries(): MutableList<SeriesManagerInfo> = seriesList

    override fun atualizarSerie(serie: SeriesManagerInfo): Int {
        criarOuAtualizarSerie(serie)
        return 1
    }

    override fun removerSerie(nome: String): Int {
        seriesFb.child(nome).removeValue()
        return 1
    }

    private fun criarOuAtualizarSerie(serie: SeriesManagerInfo){
        seriesFb.child(serie.nome).setValue(serie)
    }
}