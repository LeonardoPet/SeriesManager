package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityCadBinding;
import com.example.myapplication.model.SeriesManagerInfo;


public class CadActivity extends AppCompatActivity {
    private ActivityCadBinding  activityCadBinding;
    private int posicao = -1;
    private SeriesManagerInfo serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCadBinding = ActivityCadBinding.inflate(getLayoutInflater());
        setContentView(activityCadBinding.getRoot());

        activityCadBinding.salvar.setOnClickListener(
                (View view) -> {
                    serie = new SeriesManagerInfo(
                            activityCadBinding.nomeEt.getText().toString(),
                            activityCadBinding.AnoEt.getText().toString(),
                            activityCadBinding.GeneroEt.getText().toString(),
                            activityCadBinding.EmissoraEt.getText().toString()
                    );

                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(SerieListActivity.EXTRA_SERIE, serie);

                    // se for edição, devolver posicao
                    if (posicao != -1){
                        resultadoIntent.putExtra(SerieListActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );
        // verificar edição
        posicao = getIntent().getIntExtra(SerieListActivity.EXTRA_POSICAO,-1);
        serie = getIntent().getParcelableExtra(SerieListActivity.EXTRA_SERIE);
        if(serie != null){
            activityCadBinding.nomeEt.setEnabled(false);
            activityCadBinding.nomeEt.setText(serie.getNome());
            activityCadBinding.AnoEt.setText(serie.getAno());
            activityCadBinding.GeneroEt.setText(serie.getGenero());
            activityCadBinding.EmissoraEt.setText(serie.getEmissora());
           /* if(posicao == -1){
                activityCadBinding.nomeEt.setEnabled(false);
                activityCadBinding.AnoEt.setEnabled(false);
                activityCadBinding.GeneroEt.setEnabled(false);
                activityCadBinding.EmissoraEt.setEnabled(false);
                activityCadBinding.salvar.setVisibility(View.GONE);
            }*/
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