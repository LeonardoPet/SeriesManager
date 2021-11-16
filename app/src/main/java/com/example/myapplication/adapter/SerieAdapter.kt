package com.example.myapplication.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutSerieBinding
import com.example.myapplication.model.SeriesManagerInfo

class SerieAdapter(
    val contexto: Context,
    leiaute: Int,
    val listaSerie: MutableList<SeriesManagerInfo>
    ): ArrayAdapter<SeriesManagerInfo>(contexto,leiaute,listaSerie) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val serieLayoutView: View
        if(convertView != null){
            //célula reciclada
            serieLayoutView = convertView
        }
        else{
            //inflar célula nova
            val layoutSerieManagerInfoBinding = LayoutSerieBinding.inflate(
                contexto.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            )
            with(layoutSerieManagerInfoBinding){
                root.tag = SerieLayoutHolder(NomeTv,AnoTv,GeneroTv)
                serieLayoutView = layoutSerieManagerInfoBinding.root
            }


        }
        //Atualizar os dados da Célula
        val serie = listaSerie[position]

        val serieLayoutHolder = serieLayoutView.tag as SerieLayoutHolder
        with(serieLayoutHolder){
            NomeTv.text = serie.nome
            AnoTv.text = serie.ano
            GeneroTv.text = serie.genero
        }

        return serieLayoutView
    }

    private data class SerieLayoutHolder(
        val NomeTv: TextView,
        val AnoTv: TextView,
        val GeneroTv: TextView
    )
}