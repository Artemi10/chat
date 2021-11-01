//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.01 at 08:43:14 AM MSK 
//


package io.spring.guides.gs_producing_web_service;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the io.spring.guides.gs_producing_web_service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: io.spring.guides.gs_producing_web_service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetChatsByAdminNameRequest }
     * 
     */
    public GetChatsByAdminNameRequest createGetChatsByAdminNameRequest() {
        return new GetChatsByAdminNameRequest();
    }

    /**
     * Create an instance of {@link GetChatByNameRequest }
     * 
     */
    public GetChatByNameRequest createGetChatByNameRequest() {
        return new GetChatByNameRequest();
    }

    /**
     * Create an instance of {@link GetChatByNameResponse }
     * 
     */
    public GetChatByNameResponse createGetChatByNameResponse() {
        return new GetChatByNameResponse();
    }

    /**
     * Create an instance of {@link ChatDTO }
     * 
     */
    public ChatDTO createChatDTO() {
        return new ChatDTO();
    }

    /**
     * Create an instance of {@link GetChatsByAdminNameResponse }
     * 
     */
    public GetChatsByAdminNameResponse createGetChatsByAdminNameResponse() {
        return new GetChatsByAdminNameResponse();
    }

    /**
     * Create an instance of {@link CreateChatRequest }
     * 
     */
    public CreateChatRequest createCreateChatRequest() {
        return new CreateChatRequest();
    }

    /**
     * Create an instance of {@link CreateChatResponse }
     * 
     */
    public CreateChatResponse createCreateChatResponse() {
        return new CreateChatResponse();
    }

    /**
     * Create an instance of {@link ChatUserDTO }
     * 
     */
    public ChatUserDTO createChatUserDTO() {
        return new ChatUserDTO();
    }

}
