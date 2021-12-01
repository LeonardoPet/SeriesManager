package com.example.myapplication.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TemporadaFirebase: TemporadaDAO {
    companion object {
        private val BD_TEMPORADA = "temporada"
    }

    private val temporadaFb = Firebase.database.getReference(BD_TEMPORADA)

    private val temporadaList: MutableList<TemporadasManagerInfo> = mutableListOf()
    init {
        temporadaFb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoTemporada: TemporadasManagerInfo? = snapshot.value as? TemporadasManagerInfo
                    novoTemporada?.apply {
                        if(temporadaList.find { it.nome == novoTemporada.nome } == null){
                            temporadaList.add(novoTemporada)
                        }
                    }
              }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
               val temporadaEditado: TemporadasManagerInfo? = snapshot.value as? TemporadasManagerInfo
                temporadaEditado?.apply {
                    temporadaList[temporadaList.indexOfFirst ({it.nome == this.nome})] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val temporadaRemovido: TemporadasManagerInfo? = snapshot.value as? TemporadasManagerInfo
                temporadaRemovido.apply {
                    temporadaList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // return nada
            }

            override fun onCancelled(error: DatabaseError) {
                // return nada
            }
        })

         temporadaFb.addListenerForSingleValueEvent(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 temporadaList.clear()
                 snapshot.children.forEach{
                     val temporada: TemporadasManagerInfo = it.getValue<TemporadasManagerInfo>()?:TemporadasManagerInfo()
                     temporadaList.add(temporada)
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 // return nada
             }
         })
    }

        override fun criarTemporada(temporada: TemporadasManagerInfo): Long {
            criarOuAtualizarTemporada(temporada)
            return 0L
        }

        override fun recuperarTemporada(nome: String): TemporadasManagerInfo = temporadaList.firstOrNull{it.nome == nome} ?: TemporadasManagerInfo()

        override fun recuperarTemporadas(): MutableList<TemporadasManagerInfo> = temporadaList

        override fun atualizarTemporada(temporada: TemporadasManagerInfo): Int {
            criarOuAtualizarTemporada(temporada)
            return 1
        }

        override fun removerTemporada(nome: String): Int {
            temporadaFb.child(nome).removeValue()
            return 1
        }

    private fun criarOuAtualizarTemporada(temporada: TemporadasManagerInfo){
        temporadaFb.child(temporada.nome).setValue(temporada)
    }
}