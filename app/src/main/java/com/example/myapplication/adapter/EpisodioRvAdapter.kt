package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.EpisodioActivity

import com.example.myapplication.databinding.LayoutEpisodioBinding
import com.example.myapplication.model.EpisodioManagerInfo

class EpisodioRvAdapter(
    private val onEpisodioClickListener: EpisodioActivity,
    private val episodioList: MutableList<EpisodioManagerInfo>
    ): RecyclerView.Adapter<EpisodioRvAdapter.EpisodioLayoutHolder>(){
    var posicao: Int = -1

    //ViewHolder
    inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding): RecyclerView.ViewHolder(layoutEpisodioBinding.root){
        val numSequencialTv: TextView = layoutEpisodioBinding.numSquencialEpEt
        val nomeEpisodioTv: TextView = layoutEpisodioBinding.NomeEpEt
        val duracaoTv: TextView = layoutEpisodioBinding.duracaoEpEt

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioLayoutHolder {
       //criar nova célula
        val layoutEpisodioBinding = LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent,false)

        //cria viewholder
        val viewHolder: EpisodioLayoutHolder = EpisodioLayoutHolder(layoutEpisodioBinding)
        return viewHolder

    }

    override fun onBindViewHolder(holder: EpisodioLayoutHolder, position: Int) {
        //buscaserie
        val episodio = episodioList[position]
        //atualizar
        with(holder){
            numSequencialTv.text = episodio.numSequencialEp
            nomeEpisodioTv.text = episodio.nomeEpisodio
            duracaoTv.text = episodio.tempoDuracao
            holder.itemView.setOnClickListener{
                onEpisodioClickListener.onEpisodioClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }

    }
    override fun getItemCount(): Int = episodioList.size


}