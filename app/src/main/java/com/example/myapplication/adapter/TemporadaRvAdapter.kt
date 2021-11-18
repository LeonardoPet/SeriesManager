package com.example.myapplication.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.OnTemporadaClickListener
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutTemporadaBinding

import com.example.myapplication.model.TemporadasManagerInfo

class TemporadaRvAdapter (
    private val onTemporadaClickListener: OnTemporadaClickListener,
    private val temporadaList: MutableList<TemporadasManagerInfo>
    ): RecyclerView.Adapter<TemporadaRvAdapter.TemporadaLayoutHolder>(){
    //posicao que será recuperada pelo menu de contexto
    var posicao: Int = -1

    // ViewHolder
        inner class TemporadaLayoutHolder(layoutTemporadaBinding: LayoutTemporadaBinding): RecyclerView.ViewHolder(layoutTemporadaBinding.root),View.OnCreateContextMenuListener{
            val numSequencialTv: TextView = layoutTemporadaBinding.numSquencialTempEt
            val anoTv: TextView = layoutTemporadaBinding.anolancamentoTempEt
            val qtdEpisodioTv: TextView =  layoutTemporadaBinding.qtdEpisodioEt
            init {
                itemView.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                MenuInflater(view?.context).inflate(R.menu.context_menu_temporada,menu)
            }
        }


    // nova célula é criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemporadaLayoutHolder {
        //cria nova célula
        val layoutTemporadaBinding = LayoutTemporadaBinding.inflate(LayoutInflater.from(parent.context),parent, false)

        //cria viewholder
        val viewHolder: TemporadaLayoutHolder = TemporadaLayoutHolder(layoutTemporadaBinding)
        return viewHolder
    }

    //
    override fun onBindViewHolder(holder: TemporadaLayoutHolder, position: Int) {
        //busca uma temporada
        val temporada = temporadaList[position]

        //atualizar os valores da viewholder
        with(holder){
            numSequencialTv.text = temporada.numSequencial
            anoTv.text = temporada.ano
            qtdEpisodioTv.text = temporada.qtdEpisodio

            itemView.setOnClickListener{
                onTemporadaClickListener.onTemporadaClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }
    //
    override fun getItemCount(): Int = temporadaList.size


}

