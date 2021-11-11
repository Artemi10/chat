package com.devanmejia.chataccount.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class ChatWebServiceConfig extends WsConfigurerAdapter {

    @Bean("chat")
    public DefaultWsdl11Definition chatsWsdl11Definition(
            @Qualifier("chatsSchema") XsdSchema chatsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ChatPort");
        wsdl11Definition.setLocationUri("/api/account");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(chatsSchema);
        return wsdl11Definition;
    }

    @Bean("chatsSchema")
    public XsdSchema chatsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("/xsd/chat.xsd"));
    }

}
