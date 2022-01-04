//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.25 at 05:40:37 PM MSK 
//


package io.spring.guides.gs_producing_web_service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://spring.io/guides/gs-producing-web-service}UserInfoDTO"/>
 *         &lt;element name="isChatMember" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "isChatMember"
})
@XmlRootElement(name = "getChatUserResponse")
public class GetChatUserResponse {

    @XmlElement(required = true)
    protected UserInfoDTO user;
    protected boolean isChatMember;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link UserInfoDTO }
     *     
     */
    public UserInfoDTO getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserInfoDTO }
     *     
     */
    public void setUser(UserInfoDTO value) {
        this.user = value;
    }

    /**
     * Gets the value of the isChatMember property.
     * 
     */
    public boolean isIsChatMember() {
        return isChatMember;
    }

    /**
     * Sets the value of the isChatMember property.
     * 
     */
    public void setIsChatMember(boolean value) {
        this.isChatMember = value;
    }

}
