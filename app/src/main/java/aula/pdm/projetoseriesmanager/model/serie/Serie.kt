package aula.pdm.projetoseriesmanager.model.serie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie (
    var nome: String = "", //chave primária
    val ano: Int = 0,
    val emissora: String = "",
    val genero: String = ""
): Parcelable