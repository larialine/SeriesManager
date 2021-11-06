package aula.pdm.projetoseriesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import aula.pdm.projetoseriesmanager.databinding.ActivitySerieBinding;
import aula.pdm.projetoseriesmanager.model.serie.Serie;

public class SerieActivity extends AppCompatActivity {

    private ActivitySerieBinding activitySerieBinding;
    private int posicao = -1;
    private Serie serie;
    private ArrayList<String> generoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySerieBinding = ActivitySerieBinding.inflate(getLayoutInflater());
        setContentView(activitySerieBinding.getRoot());

        activitySerieBinding.salvarBt.setOnClickListener(
                (View view) -> {
                    serie = new Serie(
                            activitySerieBinding.nomeEt.getText().toString(),
                            Integer.parseInt(activitySerieBinding.anoEt.getText().toString()),
                            activitySerieBinding.emissoraEt.getText().toString(),
                            activitySerieBinding.generoSp.getSelectedItem().toString()
                    );

                    //retornar o livro (dados preenchido na tela) para a MainActivity
                    Intent resultadoIntent = new Intent();
                    resultadoIntent.putExtra(MainActivity.EXTRA_SERIE, serie);
                    //Se foi edição, devolver posição também
                    if(posicao != -1){
                        resultadoIntent.putExtra(MainActivity.EXTRA_POSICAO, posicao);
                    }
                    setResult(RESULT_OK, resultadoIntent);
                    finish();
                }
        );

        //Verificando se é uma edição ou consulta e preenchendo os campos
        posicao = getIntent().getIntExtra(MainActivity.EXTRA_POSICAO, -1);
        serie = getIntent().getParcelableExtra(MainActivity.EXTRA_SERIE);
        if(serie != null){
            activitySerieBinding.nomeEt.setEnabled(false);
            activitySerieBinding.nomeEt.setText(serie.getNome());
            activitySerieBinding.anoEt.setText(String.valueOf(serie.getAno()));
            activitySerieBinding.emissoraEt.setText(serie.getEmissora());

            generoList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.genero)));
            ArrayAdapter<String> generoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generoList);
            activitySerieBinding.generoSp.setAdapter(generoAdapter);

            if(posicao == -1){
                for(int i=0; i<activitySerieBinding.getRoot().getChildCount(); i++){
                    activitySerieBinding.getRoot().getChildAt(i).setEnabled(false);
                }
                activitySerieBinding.salvarBt.setVisibility(View.GONE);
            }
        }
    }
}
