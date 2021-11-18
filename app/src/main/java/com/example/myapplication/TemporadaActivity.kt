package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TemporadaRvAdapter
import com.example.myapplication.controller.TemporadaController
import com.example.myapplication.databinding.ActivityTemporadaBinding
import com.example.myapplication.model.TemporadasManagerInfo

class TemporadaActivity : AppCompatActivity(), OnTemporadaClickListener {
    companion object Extras{
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }
    private val activityMainBinding: ActivityTemporadaBinding by lazy{
        ActivityTemporadaBinding.inflate(layoutInflater)
    }

    private lateinit var temporadaActivityResultLaucher: ActivityResultLauncher<Intent>
    private lateinit var editarTemporadaActivityResultLaucher: ActivityResultLauncher<Intent>

    //Data source
    private val temporadaList: MutableList<TemporadasManagerInfo> by lazy{
       temporadaController.buscarTemporadas()
    }

    // Controller
    private val temporadaController: TemporadaController by lazy{
        TemporadaController(this)
    }

    //Adapter
    private val temporadaAdapter: TemporadaRvAdapter by lazy{
        TemporadaRvAdapter(this, temporadaList)
    }

    //layout manager
    private val temporadasLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.temporadaRecycler.adapter = temporadaAdapter
        activityMainBinding.temporadaRecycler.layoutManager = temporadasLayoutManager

        temporadaActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
            if(resultado.resultCode == RESULT_OK){
                resultado.data?.getParcelableExtra<TemporadasManagerInfo>(EXTRA_TEMPORADA)?.apply{
                    temporadaController.inserirTemporada(this)
                    temporadaList.add(this)
                    temporadaAdapter.notifyDataSetChanged()
                }
            }

        }

        editarTemporadaActivityResultLaucher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
            if(resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO,-1)
                resultado.data?.getParcelableExtra<TemporadasManagerInfo>(EXTRA_TEMPORADA)?.apply {
                    if(posicao != null && posicao != -1){
                        temporadaController.modificarTemporada(this)
                        temporadaList[posicao] = this
                        temporadaAdapter.notifyDataSetChanged()
                    }
                }
            }

        }

        activityMainBinding.adicionarTemporadaFab.setOnClickListener{temporadaActivityResultLaucher.launch(Intent(this, CadastroTemporadaActivity::class.java))}
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao
        val temporada = temporadaList[posicao]

        return when(item.itemId){
            R.id.editarTemporadaMi ->{
                //editar temporada
                val editarTemporadaIntent = Intent(this, CadastroTemporadaActivity::class.java)
                editarTemporadaIntent.putExtra(EXTRA_TEMPORADA,temporada)
                editarTemporadaIntent.putExtra(EXTRA_POSICAO, posicao)
                editarTemporadaActivityResultLaucher.launch(editarTemporadaIntent)
                true
            }
            R.id.removerTemporadaMi ->{
                //remover temporada
                temporadaController.apagarTemporada(temporada.nome)
                temporadaList.removeAt(posicao)
                temporadaAdapter.notifyDataSetChanged()
                true
            }
            else ->{ false }
        }
    }

    override fun onTemporadaClick(posicao: Int) {
        val temporada = temporadaList[posicao]
        val consultarEpidosioIntent = Intent(this, EpisodioActivity::class.java)
        startActivity(consultarEpidosioIntent)
    }
}