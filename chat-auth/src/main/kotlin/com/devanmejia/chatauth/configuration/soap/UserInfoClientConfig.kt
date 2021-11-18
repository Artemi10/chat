package com.devanmejia.chatauth.configuration.soap

import com.devanmejia.chatauth.configuration.soap.encoding.Jaxb2SoapDecoder
import com.devanmejia.chatauth.configuration.soap.encoding.Jaxb2SoapEncoder
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient;
import java.util.concurrent.TimeUnit

@Configuration
class UserInfoClientConfig {

    @Bean
    @LoadBalanced
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected { connection ->
                connection.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                connection.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }
        val exchangeStrategies = ExchangeStrategies.builder()
            .codecs { clientCodecConfigurer ->
                clientCodecConfigurer.customCodecs().register(Jaxb2SoapEncoder())
                clientCodecConfigurer.customCodecs().register(Jaxb2SoapDecoder())
            }.build()
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient.wiretap(true)))
            .exchangeStrategies(exchangeStrategies)
            .build()
    }
}
