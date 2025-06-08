package com.irjarqui.peleadoresapiaryp2.data.remote

import com.irjarqui.peleadoresapiaryp2.data.remote.model.FavoritoRequestDto
import com.irjarqui.peleadoresapiaryp2.data.remote.model.FavoritoResponseDto
import com.irjarqui.peleadoresapiaryp2.data.remote.model.PeleadorDto
import com.irjarqui.peleadoresapiaryp2.utils.Constants
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PeleadoresApi {

    @GET(Constants.ENDPOINT_PELEADORES)
    suspend fun getPeleadores(): List<PeleadorDto>

    @GET(Constants.ENDPOINT_PELEADOR_DETAIL)
    suspend fun getPeleadorDetail(@Path("id") id: Int): PeleadorDto

    @POST(Constants.ENDPOINT_PELEADORES)
    suspend fun addPeleador(@Body peleador: PeleadorDto): PeleadorDto

    @DELETE(Constants.ENDPOINT_PELEADOR_DETAIL)
    suspend fun deletePeleador(@Path("id") id: Int): retrofit2.Response<Void>

    @POST(Constants.ENDPOINT_FAVORITOS)
    suspend fun addFavorito(@Body favoritoRequest: FavoritoRequestDto): FavoritoResponseDto
}