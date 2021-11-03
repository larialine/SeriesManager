package aula.pdm.projetoseriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import aula.pdm.projetoseriesmanager.R
import java.sql.SQLException

class SerieSqlite(contexto: Context): SerieDAO {
    companion object{
        private val BD_SERIES = "series"
        private val TABELA_SERIE = "serie"
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO = "ano"
        private val COLUNA_EMISSORA = "emissora"
        private val COLUNA_GENERO = "genero"

        private val CRIAR_TABELA_SERIE_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_SERIE} (" +
                "${COLUNA_NOME} TEXT NOT NULL PRIMARY KEY," +
                "${COLUNA_ANO} INTEGER NOT NULL," +
                "${COLUNA_EMISSORA} TEXT NOT NULL," +
                "${COLUNA_GENERO} TEXT NOT NULL );"
    }

    // Referência para o banco de dados
    private val seriesBd: SQLiteDatabase
    init {
        seriesBd = contexto.openOrCreateDatabase(BD_SERIES, MODE_PRIVATE, null)
        try{
            seriesBd.execSQL(CRIAR_TABELA_SERIE_STMT)
        }catch (se: SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun criarSerie(serie: Serie): Long {
        val serieCv = converterSerieParaContentValues(serie)

        return seriesBd.insert(TABELA_SERIE, null, serieCv)
    }

    override fun recupararSerie(nome: String): Serie {
        val serieCursor = seriesBd.query(
            true,
            TABELA_SERIE,
            null,                   // parametros
            "${COLUNA_NOME} = ?", //where
            arrayOf(nome),
            null,
            null,
            null,
            null
        )

        return if(serieCursor.moveToFirst()){
            with(serieCursor){
                Serie(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getInt(getColumnIndexOrThrow(COLUNA_ANO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO))
                )
            }
        }
        else{
            Serie()
        }
    }

    override fun recuperarSeries(): MutableList<Serie> {
        val seriesList = mutableListOf<Serie>()

        val serieCursor = seriesBd.rawQuery("SELECT * FROM ${TABELA_SERIE};" , null)

        while(serieCursor.moveToNext()){
            with(serieCursor){
                seriesList.add(
                    Serie(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getInt(getColumnIndexOrThrow(COLUNA_ANO)),
                        getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                        getString(getColumnIndexOrThrow(COLUNA_GENERO))
                    )
                )
            }
        }
        return seriesList
    }

    override fun atualizarSerie(serie: Serie): Int {
        val serieCv = converterSerieParaContentValues(serie)

        return seriesBd.update(TABELA_SERIE, serieCv, "${COLUNA_NOME} = ?", arrayOf(serie.nome))
    }

    override fun removerSerie(nome: String): Int {
        return seriesBd.delete(TABELA_SERIE, "${ COLUNA_NOME} = ?", arrayOf(nome))
    }

    private fun converterSerieParaContentValues(serie: Serie) = ContentValues().also {
        with(it){
            put(COLUNA_NOME, serie.nome)
            put(COLUNA_ANO, serie.ano)
            put(COLUNA_EMISSORA, serie.emissora)
            put(COLUNA_GENERO, serie.genero)
        }
    }
}