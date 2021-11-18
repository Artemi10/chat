package com.devanmejia.chatauth.configuration.soap.encoding

import org.springframework.core.ResolvableType
import org.springframework.core.codec.CodecException
import org.springframework.core.codec.DecodingException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.xml.Jaxb2XmlDecoder
import org.springframework.util.MimeType
import org.springframework.util.xml.StaxUtils
import org.springframework.ws.WebServiceMessage
import org.springframework.ws.WebServiceMessageFactory
import org.springframework.ws.client.core.WebServiceTemplate
import org.springframework.ws.support.DefaultStrategiesHelper
import reactor.core.Exceptions
import java.rmi.UnmarshalException
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller


class Jaxb2SoapDecoder : Jaxb2XmlDecoder(){
    private val jaxbContexts = JaxbContextContainer()

    override fun decode(dataBuffer: DataBuffer, targetType: ResolvableType,
        mimeType: MimeType?, hints: MutableMap<String, Any>?): Any {
        return try {
            val helper = DefaultStrategiesHelper(WebServiceTemplate::class.java)
            val messageFactory = helper.getDefaultStrategy(WebServiceMessageFactory::class.java)
            val message = messageFactory.createWebServiceMessage(dataBuffer.asInputStream())
            unmarshal(message, targetType.toClass())
        } catch (ex: Throwable) {
            throw Exceptions.propagate(ex)
        } finally {
            DataBufferUtils.release(dataBuffer)
        }
    }

    private fun unmarshal(message: WebServiceMessage, outputClass: Class<*>): Any {
        return try {
            val unmarshaller = initUnmarshaller(outputClass)
            val jaxbElement = unmarshaller.unmarshal(message.payloadSource, outputClass)
            jaxbElement.value
        } catch (ex: UnmarshalException) {
            throw DecodingException("Could not unmarshal XML to $outputClass", ex)
        } catch (ex: JAXBException) {
            throw CodecException("Invalid JAXB configuration", ex)
        }
    }

    private fun initUnmarshaller(outputClass: Class<*>): Unmarshaller {
        val unmarshaller = jaxbContexts.createUnmarshaller(outputClass)
        return unmarshallerProcessor.apply(unmarshaller)
    }
}
