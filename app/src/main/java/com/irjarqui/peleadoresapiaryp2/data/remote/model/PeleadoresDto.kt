package com.irjarqui.peleadoresapiaryp2.data.remote.model

import com.google.gson.annotations.SerializedName

data class PeleadoresDto(
    @SerializedName("peleadores") var peleadores: List<PeleadorDto>
)