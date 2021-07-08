package br.com.zupacademy.ratkovski

import com.google.protobuf.Any
import com.google.rpc.ErrorDetailsProto
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.exceptions.HttpStatusException
import zupacademy.ratkovski.CalculaFreteRequest
import zupacademy.ratkovski.ErroDetails
import zupacademy.ratkovski.FretesServiceGrpc
import javax.inject.Inject

@Controller
class CalculadoraDeFretesController(@Inject val gRpcClient: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get("/api/fretes")
    fun calcula(cep: String): FretesResponse {
        val request = CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        try {
            val response = gRpcClient.calculaFrete(request)
            return FretesResponse(cep = response.cep, valor = response.valor)

        } catch (e:StatusRuntimeException) {

            val statusCode = e.status.code
            val description =e.status.description


            if (statusCode == Status.Code.INVALID_ARGUMENT) {
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }

            if (statusCode == Status.Code.PERMISSION_DENIED){
                val statusProto = StatusProto.fromThrowable(e)
                if (statusProto == null){
                    throw HttpStatusException(HttpStatus.FORBIDDEN, description)
                }
                val anyDetails:Any= statusProto.detailsList.get(0)
                val errorDetails = anyDetails.unpack(ErroDetails::class.java)
                throw HttpStatusException(HttpStatus.FORBIDDEN," ${errorDetails.code}:${errorDetails.message}")

            }
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)//code+message
        }
    }

    data class FretesResponse(val cep: String, val valor: Double) {

    }
}