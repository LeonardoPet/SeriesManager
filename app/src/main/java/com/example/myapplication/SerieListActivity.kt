package com.example.myapplication

import android.content.Intent
import android.os.Bundle

import android.view.MenuItem

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapplication.adapter.SerieRvAdapter
import com.example.myapplication.controller.SerieController
import com.example.myapplication.databinding.ActivitySerieListBinding
import com.example.myapplication.model.SeriesManagerInfo


class SerieListActivity : AppCompatActivity(), OnSerieClickListener {

    companion object Extras{
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }
    private val activityMainBinding: ActivitySerieListBinding by lazy{
        ActivitySerieListBinding.inflate(layoutInflater)
    }
    private lateinit var serieActivityResultLaucher: ActivityResultLauncher<Intent>
    private lateinit var editarSerieActivityResultLaucher: ActivityResultLauncher<Intent>



    //Data source
    private val seriesList: MutableList<SeriesManagerInfo> by lazy{
        serieController.buscarSeries()
    }

    // Controller
    private val serieController: SerieController by lazy{
        SerieController(this)
    }

    //Adapter
    private val serieAdapter: SerieRvAdapter by lazy{
       SerieRvAdapter(this, seriesList)
    }


    //Layout Manager
    private val seriesLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)




        // Associando o Adapter e o Recycler View
        activityMainBinding.serieRecycler.adapter = serieAdapter
        activityMainBinding.serieRecycler.layoutManager = seriesLayoutManager


        serieActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
            if(resultado.resultCode == RESULT_OK){
                resultado.data?.getParcelableExtra<SeriesManagerInfo>(EXTRA_SERIE)?.apply{
                    serieController.inserirSerie(this)
                    seriesList.add(this)
                    serieAdapter.notifyDataSetChanged()
                }
            }
        }

        editarSerieActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
            if(resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO,-1)
                resultado.data?.getParcelableExtra<SeriesManagerInfo>(EXTRA_SERIE)?.apply{
                    if(posicao != null && posicao != -1){
                        serieController.modificarSerie(this)
                        seriesList[posicao] = this
                        serieAdapter.notifyDataSetChanged()
                    }
                }
            }
        }


        activityMainBinding.adicionarSerieFab.setOnClickListener {serieActivityResultLaucher.launch(Intent(this, CadActivity::class.java))}
    }



    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = serieAdapter.posicao
        val serie = seriesList[posicao]

        return when(item.itemId) {
            R.id.editarSerieMi -> {
                //editar Série
                val editarSerieIntent = Intent(this, CadActivity::class.java)
                editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                editarSerieActivityResultLaucher.launch(editarSerieIntent)
                true
            }
            R.id.removerSerieMi -> {
                //remover série
                serieController.apagarSerie(serie.nome)
                seriesList.removeAt(posicao)
                serieAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }
    }

    override fun onSerieClick(posicao: Int) {
        val serie = seriesList[posicao]
        val consultarTemporadaIntent = Intent(this, TemporadaActivity::class.java)
        startActivity(consultarTemporadaIntent)
    }
}