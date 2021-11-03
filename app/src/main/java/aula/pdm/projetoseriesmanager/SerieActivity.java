package aula.pdm.projetoseriesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import aula.pdm.projetoseriesmanager.databinding.ActivitySerieBinding;
import aula.pdm.projetoseriesmanager.model.Serie;

public class SerieActivity extends AppCompatActivity {

    private ActivitySerieBinding activitySerieBinding;
    private int posicao = -1;
    private Serie serie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySerieBinding = ActivitySerieBinding.inflate(getLayoutInflater());
        setContentView(activitySerieBinding.getRoot());

        activitySerieBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    serie = new Serie(
                            activitySerieBinding.nomeEt.getText().toString(),
                            Integer.parseInt(activitySerieBinding.anoEt.getText().toString()),
                            activitySerieBinding.emissoraEt.getText().toString(),
                            ((TextView)activitySerieBinding.generoSp.getSelectedView()).getText().toString()
                    );

                    // retornar a série (dados preenchido na tela) para a MainActivity
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_SERIE, serie);
                    // se for edição, devolver posição também
                    if(posicao != -1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        // Verificando se é uma edição ou consulta e preenchendo os dados
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        serie = getIntent().getParcelableExtra(MainActivity.EXTRA_SERIE);
        if(serie != null){
            activitySerieBinding.nomeEt.setEnabled(false);
            activitySerieBinding.anoEt.setText(serie.getAno());
            activitySerieBinding.emissoraEt.setText(serie.getEmissora());
            ((TextView)activitySerieBinding.generoSp.getSelectedView()).getText();
            if(posicao == -1){
                for(int i=0; i<activitySerieBinding.getRoot().getChildCount(); i++){
                    activitySerieBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activitySerieBinding.salvarBt.setVisibility(View.GONE);
            }
        }

    }
}
