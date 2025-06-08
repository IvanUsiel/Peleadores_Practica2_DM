package com.irjarqui.peleadoresapiaryp2.data.remote.model

data class FavoritoRequestDto(
    val usuario: String,
    val peleadorId: Int,
    val mensaje: String
)