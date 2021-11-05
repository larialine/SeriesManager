package aula.pdm.projetoseriesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import aula.pdm.projetoseriesmanager.databinding.ActivityTemporadaBinding;
import aula.pdm.projetoseriesmanager.model.temporada.Temporada;

public class TemporadaActivity extends AppCompatActivity {

    private ActivityTemporadaBinding activityTemporadaBinding;
    private int posicao = -1;
    private Temporada temporada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTemporadaBinding = ActivityTemporadaBinding.inflate(getLayoutInflater());
        setContentView(activityTemporadaBinding.getRoot());

        activityTemporadaBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    temporada = new Temporada(
                            Integer.parseInt(activityTemporadaBinding.numeroEt.getText().toString()),
                            Integer.parseInt(activityTemporadaBinding.anoEt.getText().toString()),
                            Integer.parseInt(activityTemporadaBinding.qtdeEpisodiosEt.getText().toString())
                    );

                    // Retornar temporada (dados preenchido na tela) para MainTemporadaActivity
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada);
                    //Se foi edição, devolver posição também
                    if(posicao != -1){
                        resultadoIntent.putExtra(MainTemporadaActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        // Verificando se é uma edição ou consulta e preenchendo os campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        temporada = getIntent().getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA);
        if(temporada != null){
            activityTemporadaBinding.numeroEt.setText(String.valueOf(temporada.getNumero()));
            activityTemporadaBinding.anoEt.setText(String.valueOf(temporada.getAno()));
            activityTemporadaBinding.qtdeEpisodiosEt.setText(String.valueOf(temporada.getEpisodios()));
            if(posicao == -1){
                for(int i=0; i<activityTemporadaBinding.getRoot().getChildCount(); i++){
                    activityTemporadaBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityTemporadaBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
