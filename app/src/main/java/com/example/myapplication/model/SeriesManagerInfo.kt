package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeriesManagerInfo(
    val nome: String = "", //chavePrimaria
    val ano: String = "",
    val genero: String = "",
    val emissora: String = ""
):Parcelable

