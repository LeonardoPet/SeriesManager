package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityCadastroTemporadaBinding;
import com.example.myapplication.model.TemporadasManagerInfo;

public class CadastroTemporadaActivity extends AppCompatActivity {
    private ActivityCadastroTemporadaBinding activityCadastroTemporadaBinding;
    private int posicao = -1;
    private TemporadasManagerInfo temporada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCadastroTemporadaBinding = ActivityCadastroTemporadaBinding.inflate(getLayoutInflater());
        setContentView(activityCadastroTemporadaBinding.getRoot());

        activityCadastroTemporadaBinding.salvarTemporadaBt.setOnClickListener(
                (View view) ->{
                   temporada = new TemporadasManagerInfo(
                           activityCadastroTemporadaBinding.nomeTempEt.getText().toString(),
                           activityCadastroTemporadaBinding.numSquencialtempEt.getText().toString(),
                           activityCadastroTemporadaBinding.anolancamentoTempEt.getText().toString(),
                           activityCadastroTemporadaBinding.qtdEpisodioEt.getText().toString()

                    );
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(TemporadaActivity.EXTRA_TEMPORADA, temporada);

                    if (posicao != -1){
                        resultadoIntent.putExtra(TemporadaActivity.EXTRA_TEMPORADA,temporada);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
            }
        );

        posicao =  getIntent().getIntExtra(SerieListActivity.EXTRA_POSICAO,-1);
        temporada = getIntent().getParcelableExtra(TemporadaActivity.EXTRA_TEMPORADA);
        if(temporada!=null){
            activityCadastroTemporadaBinding.nomeTempEt.setEnabled(false);
            activityCadastroTemporadaBinding.numSquencialtempEt.setEnabled(false);
            activityCadastroTemporadaBinding.nomeTempEt.setText(temporada.getNome());
            activityCadastroTemporadaBinding.numSquencialtempEt.setText(temporada.getNumSequencial());
            activityCadastroTemporadaBinding.anolancamentoTempEt.setText(temporada.getAno());
            activityCadastroTemporadaBinding.qtdEpisodioEt.setText(temporada.getQtdEpisodio());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(AutenticacaoFirebase.INSTANCE.getFirebaseAuth().getCurrentUser() == null){
            finish();
        }
    }
}