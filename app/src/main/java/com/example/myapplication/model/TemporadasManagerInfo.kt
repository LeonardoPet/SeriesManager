package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TemporadasManagerInfo(
    val nome: String = "", //chavePrimaria
    val numSequencial: String = "",
    val ano: String = "",
    val qtdEpisodio: String = ""
): Parcelable
