package br.com.zupacademy.ratkovski

import br.com.zup.edu.CalculaFreteRequest
import br.com.zup.edu.FretesServiceGrpc
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject

@Controller
class CalculadoraDeFretesController(@Inject val gRpcClient:FretesServiceGrpc.FretesServiceBlockingStub){

    @Get("/api/fretes")
    fun calcula(cep: String):FretesResponse{
        val request= CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()
       val response =gRpcClient.calculaFrete(request)
        return  FretesResponse(cep = response.cep,valor = response.valor)

    }

}

data class FretesResponse(val cep: String, val valor:Double) {

}
