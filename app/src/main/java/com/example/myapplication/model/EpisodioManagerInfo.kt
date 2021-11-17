package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodioManagerInfo(
    val nome: String = "", //chavePrimaria
    val numSequencialEp: String = "",
    val nomeEpisodio: String = "",
    val tempoDuracao: String = ""
): Parcelable
