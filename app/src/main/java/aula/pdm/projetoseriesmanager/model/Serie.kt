package aula.pdm.projetoseriesmanager.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie (
    val nome: String = "", //chave primária
    val ano: Int = 0,
    val emissora: String = "",
    val genero: String = ""
): Parcelable