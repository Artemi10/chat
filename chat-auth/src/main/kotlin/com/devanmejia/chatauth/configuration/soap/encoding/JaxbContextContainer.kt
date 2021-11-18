package com.devanmejia.chatauth.configuration.soap.encoding

import java.util.concurrent.ConcurrentHashMap
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

class JaxbContextContainer {
    private val jaxbContexts = ConcurrentHashMap<Class<*>, JAXBContext>(64)

    fun createMarshaller(clazz: Class<*>): Marshaller {
        val jaxbContext = getJaxbContext(clazz)
        return jaxbContext.createMarshaller()
    }

    fun createUnmarshaller(clazz: Class<*>): Unmarshaller {
        val jaxbContext = getJaxbContext(clazz)
        return jaxbContext.createUnmarshaller()
    }

    private fun getJaxbContext(clazz: Class<*>): JAXBContext {
        var jaxbContext = jaxbContexts[clazz]
        return if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(clazz)
            jaxbContexts.putIfAbsent(clazz, jaxbContext)
            jaxbContext
        } else jaxbContext
    }
}
