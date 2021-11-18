package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityCadastroEpisodioBinding;
import com.example.myapplication.model.EpisodioManagerInfo;

public class CadastroEpisodioActivity extends AppCompatActivity {
    private ActivityCadastroEpisodioBinding activityCadastroEpisodioBinding;
    private int posicao = -1;
    private EpisodioManagerInfo episodio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCadastroEpisodioBinding = ActivityCadastroEpisodioBinding.inflate(getLayoutInflater());
        setContentView(activityCadastroEpisodioBinding.getRoot());

        activityCadastroEpisodioBinding.salvarEpisodioBt.setOnClickListener(
                (View view) ->{
                    episodio = new EpisodioManagerInfo(
                            activityCadastroEpisodioBinding.numSquencialEpEt.getText().toString(),
                            activityCadastroEpisodioBinding.NomeEpEt.getText().toString(),
                            activityCadastroEpisodioBinding.duracaoEpEt.getText().toString(),
                            activityCadastroEpisodioBinding.nomeSerieEpEt.getText().toString()
                            );

                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(EpisodioActivity.EXTRA_EPISODIO, episodio);

                    if (posicao != -1){
                        resultadoIntent.putExtra(EpisodioActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        posicao = getIntent().getIntExtra(EpisodioActivity.EXTRA_POSICAO,-1);
        episodio = getIntent().getParcelableExtra(EpisodioActivity.EXTRA_EPISODIO);
        if(episodio != null){
            activityCadastroEpisodioBinding.nomeSerieEpEt.setEnabled(false);
            activityCadastroEpisodioBinding.numSquencialEpEt.setEnabled(false);
            activityCadastroEpisodioBinding.nomeSerieEpEt.setText(episodio.getNome());
            activityCadastroEpisodioBinding.NomeEpEt.setText(episodio.getNomeEpisodio());
            activityCadastroEpisodioBinding.duracaoEpEt.setText(episodio.getTempoDuracao());
            activityCadastroEpisodioBinding.numSquencialEpEt.setText(episodio.getNumSequencialEp());

        }
    }
}