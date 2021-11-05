package aula.pdm.projetoseriesmanager.model.temporada

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import aula.pdm.projetoseriesmanager.R

class TemporadaSqlite(context: Context): TemporadaDAO {

    companion object{
        private val BD_TEMPORADAS = "temporadas"
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NUMERO = "numero"
        private val COLUNA_ANO = "ano"
        private val COLUNA_EPISODIOS = "episodios"

        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_TEMPORADA} (" +
                "${COLUNA_NUMERO} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "${COLUNA_ANO} INTEGER NOT NULL," +
                "${COLUNA_EPISODIOS} INTEGER NOT NULL );"
    }

    // Referência para o banco de dados
    private val temporadasBd: SQLiteDatabase
    init {
        temporadasBd = context.openOrCreateDatabase(BD_TEMPORADAS, MODE_PRIVATE, null)
        try{
            temporadasBd.execSQL(CRIAR_TABELA_TEMPORADA_STMT)
        }catch (se: SQLException){
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun criarTemporada(temporada: Temporada): Long {
        val temporadaCv = converterTemporadaParaContentValues(temporada)

        return temporadasBd.insert(TABELA_TEMPORADA, null, temporadaCv)
    }

    override fun recuperarTemporada(numero: Int): Temporada {
        val temporadaCursor = temporadasBd.query(
            true,           //distinct
            TABELA_TEMPORADA, // tabela
            null,        // parametros
            "${COLUNA_NUMERO} = ?",  // where
            arrayOf(numero.toString()),    //valores do where
            null,
            null,
            null,
            null
        )

        return if (temporadaCursor.moveToFirst()){
            with(temporadaCursor){
                Temporada(
                    getInt(getColumnIndexOrThrow(COLUNA_NUMERO)),
                    getInt(getColumnIndexOrThrow(COLUNA_ANO)),
                    getInt(getColumnIndexOrThrow(COLUNA_EPISODIOS))
                )
            }
        }
        else{
            Temporada()
        }
    }

    override fun recuperarTemporadas(): MutableList<Temporada> {
        val temporadasList = mutableListOf<Temporada>()

        val temporadasCursor = temporadasBd.rawQuery("SELECT * FROM ${TABELA_TEMPORADA};", null)

        while(temporadasCursor.moveToNext()){
            with(temporadasCursor){
                temporadasList.add(
                    Temporada(
                        getInt(getColumnIndexOrThrow(COLUNA_NUMERO)),
                        getInt(getColumnIndexOrThrow(COLUNA_ANO)),
                        getInt(getColumnIndexOrThrow(COLUNA_EPISODIOS))
                    )
                )
            }
        }
        return temporadasList
    }

    override fun atualizarTemporada(temporada: Temporada): Int {
        val temporadaCv = converterTemporadaParaContentValues(temporada)

        return temporadasBd.update(TABELA_TEMPORADA, temporadaCv, "${COLUNA_NUMERO} = ?", arrayOf(temporada.numero.toString()))
    }

    override fun removerTemporada(numero: Int): Int {
        return temporadasBd.delete(TABELA_TEMPORADA, "${COLUNA_ANO} = ?", arrayOf(numero.toString()))
    }

    private fun converterTemporadaParaContentValues(temporada: Temporada) = ContentValues().also {
        with(it){
            put(COLUNA_NUMERO, temporada.numero)
            put(COLUNA_ANO, temporada.ano)
            put(COLUNA_EPISODIOS, temporada.episodios)
        }
    }
}