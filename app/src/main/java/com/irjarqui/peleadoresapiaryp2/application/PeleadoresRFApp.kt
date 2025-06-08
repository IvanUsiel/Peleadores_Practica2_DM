package com.irjarqui.peleadoresapiaryp2.application

import android.app.Application
import com.irjarqui.peleadoresapiaryp2.data.PeleadorRepository
import com.irjarqui.peleadoresapiaryp2.data.remote.RetrofitHelper

class PeleadoresRFApp : Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        PeleadorRepository(retrofit)
    }

}