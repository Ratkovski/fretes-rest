package br.com.zupacademy.ratkovski

import br.com.zup.edu.FretesServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

   // @Bean
    @Singleton
    fun fretesClientStub(@GrpcChannel("fretes")channel : ManagedChannel):FretesServiceGrpc.FretesServiceBlockingStub? {
//        val channel:ManagedChannel = ManagedChannelBuilder
//            .forAddress("localhost",50051)
//            .usePlaintext()
//            .maxRetryAttempts(10)
//            .build()
        return FretesServiceGrpc.newBlockingStub(channel)
    }
}