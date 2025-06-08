package com.irjarqui.peleadoresapiaryp2.data

import com.irjarqui.peleadoresapiaryp2.data.remote.PeleadoresApi
import com.irjarqui.peleadoresapiaryp2.data.remote.model.FavoritoRequestDto
import com.irjarqui.peleadoresapiaryp2.data.remote.model.FavoritoResponseDto
import com.irjarqui.peleadoresapiaryp2.data.remote.model.PeleadorDto
import retrofit2.Response
import retrofit2.Retrofit

class PeleadorRepository(private val api: Retrofit) {

    private val peleadoresApi = api.create(PeleadoresApi::class.java)
    suspend fun getPeleadores(): List<PeleadorDto> = peleadoresApi.getPeleadores()
    suspend fun getPeleadorDetail(id: Int): PeleadorDto = peleadoresApi.getPeleadorDetail(id)
    suspend fun deletePeleador(id: Int): Response<Void>{
        return peleadoresApi.deletePeleador(id)
    }
    suspend fun addFavorito(favoritoRequest: FavoritoRequestDto): FavoritoResponseDto = peleadoresApi.addFavorito(favoritoRequest)

}