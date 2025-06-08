package com.irjarqui.peleadoresapiaryp2.data.remote.model

import com.google.gson.annotations.SerializedName

data class PeleadorDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("disciplina") val disciplina: String,
    @SerializedName("peso") val peso: String,
    @SerializedName("titulos") val titulos: String,
    @SerializedName("biografia") val biografia: String,
    @SerializedName("imagen") val imagen: String,
    @SerializedName("record") val record: String,
    @SerializedName("video") val video: String,
    @SerializedName("loc") val loc: String,
    @SerializedName("nacimiento") val nacimiento: String,
    var isFavorite: Boolean = false
)