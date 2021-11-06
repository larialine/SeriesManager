package aula.pdm.projetoseriesmanager.model.episodio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio (
    val numero: Int = 0, // chave primária
    val nome: String = "",
    val duracao: Int = 0,
    val assistido: Int = 0 // 1 - true e 0 - false
): Parcelable