package com.devanmejia.chatauth.configuration.soap.encoding

import com.devanmejia.chatauth.configuration.soap.UserInfoClientRequest
import io.spring.guides.gs_producing_web_service.GetUserInfoRequest
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.core.codec.CodecException
import org.springframework.core.codec.Encoder
import org.springframework.core.codec.EncodingException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.PooledDataBuffer
import org.springframework.util.ClassUtils
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import org.springframework.ws.WebServiceMessageFactory
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.soap.SoapMessage
import org.springframework.ws.support.DefaultStrategiesHelper
import org.springframework.xml.transform.StringSource
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import javax.xml.bind.JAXBException
import javax.xml.bind.MarshalException
import javax.xml.bind.Marshaller
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType
import javax.xml.transform.TransformerFactory


class Jaxb2SoapEncoder : Encoder<Any> {
    private val jaxbContexts = JaxbContextContainer()

    override fun canEncode(elementType: ResolvableType, mimeType: MimeType?): Boolean {
        val outputClass: Class<*> = elementType.toClass()
        return outputClass.isAnnotationPresent(XmlRootElement::class.java) ||
                outputClass.isAnnotationPresent(XmlType::class.java)
    }

    override fun encode(inputStream: Publisher<out Any>, bufferFactory: DataBufferFactory,
        elementType: ResolvableType, mimeType: MimeType?, hints: MutableMap<String, Any>?): Flux<DataBuffer> {
        return Flux.from(inputStream)
            .take(1)
            .concatMap { value -> encode(value, bufferFactory, elementType, mimeType, hints) }
            .doOnDiscard(PooledDataBuffer::class.java, PooledDataBuffer::release)
    }

    override fun getEncodableMimeTypes(): List<MimeType> {
        return listOf(MimeTypeUtils.TEXT_XML)
    }


    private fun encode(value: Any, bufferFactory: DataBufferFactory, type: ResolvableType,
                       mimeType: MimeType?, hints: Map<String, Any>?): Flux<DataBuffer> {
        return Mono.fromCallable {
            var release = true
            val buffer: DataBuffer = bufferFactory.allocateBuffer(1024)
            try {
                val userInfoClientRequest = value as UserInfoClientRequest<*>
                val outputStream = buffer.asOutputStream()
                val clazz = ClassUtils.getUserClass(userInfoClientRequest.body!!)
                val marshaller = initMarshaller(clazz)
                val helper = DefaultStrategiesHelper(WebServiceTemplate::class.java)
                val messageFactory = helper.getDefaultStrategy(WebServiceMessageFactory::class.java)
                val message = messageFactory.createWebServiceMessage()

                val soapMessage = message as SoapMessage
                val header = soapMessage.soapHeader
                val headerSource = StringSource(userInfoClientRequest.headerContent)
                val transformer = TransformerFactory.newInstance().newTransformer()
                transformer.transform(headerSource, header.result)

                marshaller.marshal(userInfoClientRequest.body, message.payloadResult)
                message.writeTo(outputStream)
                release = false
                return@fromCallable buffer
            } catch (ex: MarshalException) {
                throw EncodingException("Could not marshal ${value.javaClass} to XML", ex)
            } catch (ex: JAXBException) {
                throw CodecException("Invalid JAXB configuration", ex)
            } finally {
                if (release) DataBufferUtils.release(buffer)

            }
        }.flux()
    }

    private fun initMarshaller(clazz: Class<*>): Marshaller {
        val marshaller = jaxbContexts.createMarshaller(clazz)
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name())
        return marshaller
    }
}
