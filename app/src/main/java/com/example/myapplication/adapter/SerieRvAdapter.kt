package com.example.myapplication.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.OnSerieClickListener
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutSerieBinding
import com.example.myapplication.model.SeriesManagerInfo

class SerieRvAdapter(
    private val onSerieClickListener: OnSerieClickListener,
    private val seriesList: MutableList<SeriesManagerInfo>

    ): RecyclerView.Adapter<SerieRvAdapter.SerieLayoutHolder>() {
    //posicao que será recuperada pelo menu de contexto
    var posicao: Int = -1


    //Viewholder
    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding): RecyclerView.ViewHolder(layoutSerieBinding.root),View.OnCreateContextMenuListener{
        val nomeTv: TextView = layoutSerieBinding.NomeTv
        val anoTv: TextView = layoutSerieBinding.AnoTv
        val generoTv: TextView = layoutSerieBinding.GeneroTv
        init{
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_serie, menu)
        }
    }

    //quando a célula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        val layoutSerieBinding =  LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //criar um viewholder
        val viewHolder: SerieLayoutHolder = SerieLayoutHolder(layoutSerieBinding)
        return viewHolder

    }

    // atualizar valores de uma célula
    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
        //busca a serie
        val serie = seriesList[position]
        //atualizar os valores do viewholder
        with(holder){
            nomeTv.text = serie.nome
            anoTv.text = serie.ano
            generoTv.text = serie.genero
            itemView.setOnClickListener{
                onSerieClickListener.onSerieClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }

    }

    override fun getItemCount(): Int = seriesList.size

}