package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.EpisodioRvAdapter
import com.example.myapplication.controller.EpisodioController
import com.example.myapplication.databinding.ActivityEpisodioBinding
import com.example.myapplication.model.EpisodioManagerInfo

class EpisodioActivity : AppCompatActivity(), OnEpisodioClickListener {
    companion object Extras{
        const val EXTRA_EPISODIO = "EXTRA_EPISODIO"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }
    private val activityMainBinding: ActivityEpisodioBinding by lazy{
        ActivityEpisodioBinding.inflate(layoutInflater)
    }
    private lateinit var episodioActivityResultLaucher: ActivityResultLauncher<Intent>
    private lateinit var editarEpisodioActivityResultLaucher: ActivityResultLauncher<Intent>

    //data Source
    private val episodioList: MutableList<EpisodioManagerInfo> by lazy{
        episodioController.buscarEpisodios()
    }

    // Controller
    private val episodioController: EpisodioController by lazy{
        EpisodioController(this)
    }

    //Adapter
    private val episodioAdapter: EpisodioRvAdapter by lazy{
        EpisodioRvAdapter(this, episodioList)
    }


    //Layout Manager
    private val episodioLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        // Associando o Adapter e o Recycler View
        activityMainBinding.EpisodioRecycler.adapter = episodioAdapter
        activityMainBinding.EpisodioRecycler.layoutManager = episodioLayoutManager

        registerForContextMenu(activityMainBinding.EpisodioRecycler)
        episodioActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
            if(resultado.resultCode == RESULT_OK){
                resultado.data?.getParcelableExtra<EpisodioManagerInfo>(EXTRA_EPISODIO)?.apply{
                    episodioController.inserirEpisodio(this)
                    episodioList.add(this)
                    episodioAdapter.notifyDataSetChanged()
                }
            }
        }

        editarEpisodioActivityResultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {resultado ->
            if(resultado.resultCode == RESULT_OK){
                val posicao = resultado.data?.getIntExtra(SerieListActivity.EXTRA_POSICAO,-1)
                resultado.data?.getParcelableExtra<EpisodioManagerInfo>(EXTRA_EPISODIO)?.apply{
                    if(posicao != null && posicao != -1){
                        episodioController.modificarEpisodio(this)
                        episodioList[posicao] = this
                        episodioAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarEpisodioFab.setOnClickListener {episodioActivityResultLaucher.launch(Intent(this, CadastroEpisodioActivity::class.java))}
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = episodioAdapter.posicao
        val episodio = episodioList[posicao]

        return when(item.itemId){
            R.id.editarEpisodioMi ->{
                //editar episodio
                val editarEpisodioIntent = Intent(this, CadastroEpisodioActivity::class.java)
                editarEpisodioIntent.putExtra(EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(EXTRA_POSICAO, posicao)
                editarEpisodioActivityResultLaucher.launch(editarEpisodioIntent)
                true
            }
            R.id.removerEpisodioMi ->{
                //remover episodio
                episodioController.apagarEpisodio(episodio.nome)
                episodioList.removeAt(posicao)
                episodioAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }
    }

    override fun onEpisodioClick(posicao: Int) {
        val episodio = episodioList[posicao]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.atualizarEpisodioMi -> {
            episodioAdapter.notifyDataSetChanged()
            true
        }R.id.sairEpisodioMi ->{
            AutenticacaoFirebase.firebaseAuth.signOut()
            finish()
            true
        }
        else -> {false}
    }


    override fun onStart() {
        super.onStart()
        if(AutenticacaoFirebase.firebaseAuth.currentUser == null){
            finish()
        }
    }
}