package aula.pdm.projetoseriesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import aula.pdm.projetoseriesmanager.databinding.ActivityEpisodioBinding;
import aula.pdm.projetoseriesmanager.model.episodio.Episodio;

public class EpisodioActivity extends AppCompatActivity {

    private ActivityEpisodioBinding activityEpisodioBinding;
    private int posicao = -1;
    private Episodio episodio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEpisodioBinding = ActivityEpisodioBinding.inflate(getLayoutInflater());
        setContentView(activityEpisodioBinding.getRoot());

        activityEpisodioBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    episodio = new Episodio(
                            Integer.parseInt(activityEpisodioBinding.numeroEpisodioEt.getText().toString()),
                            activityEpisodioBinding.nomeEpisodioEt.getText().toString(),
                            Integer.parseInt(activityEpisodioBinding.duracaoEpisodioEt.getText().toString()),
                            activityEpisodioBinding.flagAssistidoCb.isChecked() ? true : false
                            //Integer.parseInt(activityEpisodioBinding.flagAssistidoCb.getText().toString())
                    );

                    // Retornar episódio (dados preenchido na tela) para MainTemporadaActivity
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio);
                    //Se foi edição, devolver posição também
                    if(posicao != -1){
                        resultadoIntent.putExtra(MainEpisodioActivity.EXTRA_POSICAO_EPISODIO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        // Verificando se é uma edição ou consulta e preenchendo os campos
        posicao = getIntent().getIntExtra(MainEpisodioActivity.EXTRA_POSICAO_EPISODIO, -1);
        episodio = getIntent().getParcelableExtra(MainEpisodioActivity.EXTRA_EPISODIO);
        if(episodio != null){
            activityEpisodioBinding.numeroEpisodioEt.setEnabled(false);
            activityEpisodioBinding.numeroEpisodioEt.setText(String.valueOf(episodio.getNumero()));
            activityEpisodioBinding.nomeEpisodioEt.setEnabled(false);
            activityEpisodioBinding.nomeEpisodioEt.setText(episodio.getNome());
            activityEpisodioBinding.duracaoEpisodioEt.setEnabled(false);
            activityEpisodioBinding.duracaoEpisodioEt.setText(String.valueOf(episodio.getDuracao()));
            if(activityEpisodioBinding.flagAssistidoCb.isChecked()){
                activityEpisodioBinding.flagAssistidoCb.setSelected(true);
            }else{
                activityEpisodioBinding.flagAssistidoCb.setSelected(false);
            }
            //activityEpisodioBinding.flagAssistidoCb.setText(String.valueOf(episodio.getAssistido()));
            if(posicao == -1){
                for(int i=0; i<activityEpisodioBinding.getRoot().getChildCount(); i++){
                    activityEpisodioBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activityEpisodioBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
