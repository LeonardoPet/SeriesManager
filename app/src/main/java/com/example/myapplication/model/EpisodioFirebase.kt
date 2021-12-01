package com.example.myapplication.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EpisodioFirebase: EpisodioDAO {
    companion object {
        private val BD_EPISODIO = "episodio"
    }

    private  val episodioFb = Firebase.database.getReference(BD_EPISODIO)

    private  val episodioList: MutableList<EpisodioManagerInfo> = mutableListOf()
    init {
        episodioFb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoEpisodio: EpisodioManagerInfo? = snapshot.value as? EpisodioManagerInfo
                    novoEpisodio?.apply{
                        if(episodioList.find{it.nome == novoEpisodio.nome} == null){
                            episodioList.add(novoEpisodio)
                        }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val episodioEditado: EpisodioManagerInfo? = snapshot.value as? EpisodioManagerInfo
                episodioEditado?.apply {
                    episodioList[episodioList.indexOfFirst ({it.nome == this.nome})] == this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val episodioRemovido: EpisodioManagerInfo? = snapshot.value as? EpisodioManagerInfo
                episodioRemovido?.apply {
                    episodioList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //return nada
            }

            override fun onCancelled(error: DatabaseError) {
               //return nada
            }
        })

        episodioFb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                episodioList.clear()
                snapshot.children.forEach{
                    val episodio: EpisodioManagerInfo = it.getValue<EpisodioManagerInfo>()?: EpisodioManagerInfo()
                    episodioList.add(episodio)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                 // return nada
            }
        })
    }



    override fun criarEpisodio(episodio: EpisodioManagerInfo): Long {
        criarOuAtualizarEpisodio(episodio)
        return 0L
    }

    override fun recuperarEpisodio(nome: String): EpisodioManagerInfo = episodioList.firstOrNull { it.nome == nome } ?: EpisodioManagerInfo()

    override fun recuperarEpisodios(): MutableList<EpisodioManagerInfo> = episodioList

    override fun atualizarEpisodio(episodio: EpisodioManagerInfo): Int {
        criarOuAtualizarEpisodio(episodio)
        return 1
    }

    override fun removerEpisodio(nome: String): Int {
       episodioFb.child(nome).removeValue()
        return 1
    }

    private fun criarOuAtualizarEpisodio(episodio: EpisodioManagerInfo){
        episodioFb.child(episodio.nome).setValue(episodio)
    }
}