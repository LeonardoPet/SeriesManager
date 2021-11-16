package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.SerieAdapter
import com.example.myapplication.adapter.SerieRvAdapter
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
    private val seriesList: MutableList<SeriesManagerInfo> = mutableListOf()

    //Adapter
 /*   private val serieAdapter: ArrayAdapter<String> by lazy{
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seriesList.run{
            val seriesStringList = mutableListOf<String>()
            this.forEach{serie -> seriesStringList.add(serie.toString())}
            seriesStringList
        })
    }*/



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


        inicializarSerieList()

        // Associando o Adapter e o Recycler View
        activityMainBinding.serieRecycler.adapter = serieAdapter
        activityMainBinding.serieRecycler.layoutManager = seriesLayoutManager


        serieActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
            if(resultado.resultCode == RESULT_OK){
                resultado.data?.getParcelableExtra<SeriesManagerInfo>(EXTRA_SERIE)?.apply{
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
                        seriesList[posicao] = this
                        serieAdapter.notifyDataSetChanged()
                    }

                }
            }
        }


        activityMainBinding.adicionarSerieFab.setOnClickListener {serieActivityResultLaucher.launch(Intent(this, CadActivity::class.java))}
    }


    private fun inicializarSerieList(){
        for (indice in 1..10){
            seriesList.add(
                SeriesManagerInfo(
                    "Nome ${indice}",
                    "Ano ${indice}",
                    "Gênero ${indice}",
                    "Emissora ${indice}"
                )
            )
        }
    }



    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = serieAdapter.posicao

        return when(item.itemId) {
            R.id.editarSerieMi -> {
                //editar Série
                val serie = seriesList[posicao]
                val editarSerieIntent = Intent(this, CadActivity::class.java)
                editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                editarSerieActivityResultLaucher.launch(editarSerieIntent)

                true
            }
            R.id.removerSerieMi -> {
                //remover série
                seriesList.removeAt(posicao)
                serieAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }

        }
        val serie = seriesList[posicao]

        Toast.makeText(this, "${serie}", Toast.LENGTH_SHORT).show()
        return super.onContextItemSelected(item)
    }

    override fun onSerieClick(posicao: Int) {
        val serie = seriesList[posicao]
        val consultarSerieIntent = Intent(this, CadActivity::class.java)
        consultarSerieIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(consultarSerieIntent)
    }
}