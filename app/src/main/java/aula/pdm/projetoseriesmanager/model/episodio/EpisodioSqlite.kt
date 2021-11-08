package aula.pdm.projetoseriesmanager.model.episodio

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import aula.pdm.projetoseriesmanager.R

class EpisodioSqlite(context: Context): EpisodioDAO {

    companion object{
        private val BD_EPISODIOS = "episodios"
        private val TABELA_EPISODIO = "episodio"
        private val COLUNA_NUMERO = "numero"
        private val COLUNA_NOME = "nome"
        private val COLUNA_DURACAO = "duracao"
        private val COLUNA_ASSISTIDO = "assistido"

        private val CRIAR_TABELA_EPISODIO_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_EPISODIO} (" +
                "${COLUNA_NUMERO} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "${COLUNA_NOME} INTEGER NOT NULL," +
                "${COLUNA_DURACAO} INTEGER NOT NULL," +
                "${COLUNA_ASSISTIDO} INTEGER NOT NULL );"
    }

    // Referência para o banco de dados
    private val episodiosBd: SQLiteDatabase
    init {
        episodiosBd = context.openOrCreateDatabase(BD_EPISODIOS, MODE_PRIVATE, null)
        try{
            episodiosBd.execSQL(CRIAR_TABELA_EPISODIO_STMT)
        }catch (se: SQLException){
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun criarEpisodio(episodio: Episodio): Long {
        val episodioCv = converterEpisodioParaContentValues(episodio)

        return episodiosBd.insert(TABELA_EPISODIO, null, episodioCv)
    }

    override fun recuperarEpisodio(numero: Int): Episodio {
        val episodioCursor = episodiosBd.query(
            true,
            TABELA_EPISODIO, // tabela
            null,        // parametros
            "${COLUNA_NUMERO} = ?",  // where
            arrayOf(numero.toString()),
            null,
            null,
            null,
            null
        )

        return if(episodioCursor.moveToFirst()){
            with(episodioCursor){
                Episodio(
                    getInt(getColumnIndexOrThrow(COLUNA_NUMERO)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getInt(getColumnIndexOrThrow(COLUNA_DURACAO)),
                    getInt(getColumnIndexOrThrow(COLUNA_ASSISTIDO))
                )
            }
        }
        else{
            Episodio()
        }
    }

    override fun recuperarEpisodios(): MutableList<Episodio> {
        val episodiosCursor = episodiosBd.rawQuery("SELECT * FROM ${TABELA_EPISODIO};", null)

        val episodiosList = mutableListOf<Episodio>()

        while(episodiosCursor.moveToNext()){
            with(episodiosCursor){
                episodiosList.add(
                    Episodio(
                        getInt(getColumnIndexOrThrow(COLUNA_NUMERO)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getInt(getColumnIndexOrThrow(COLUNA_DURACAO)),
                        getInt(getColumnIndexOrThrow(COLUNA_ASSISTIDO))
                    )
                )
            }
        }
        return episodiosList
    }

    override fun atualizarEpisodio(episodio: Episodio): Int {
        val episodioCv = converterEpisodioParaContentValues(episodio)

        return episodiosBd.update(TABELA_EPISODIO, episodioCv, "${COLUNA_NUMERO} = ?", arrayOf(episodio.numero.toString()))
    }

    override fun removerEpisodio(numero: Int): Int {
        return episodiosBd.delete(TABELA_EPISODIO, "${COLUNA_NUMERO} = ?", arrayOf(numero.toString()))
    }

    private fun converterEpisodioParaContentValues(episodio: Episodio) = ContentValues().also {
        with(it){
            put(COLUNA_NUMERO, episodio.numero)
            put(COLUNA_NOME, episodio.nome)
            put(COLUNA_DURACAO, episodio.duracao)
            put(COLUNA_ASSISTIDO, episodio.assistido)
        }
    }
}