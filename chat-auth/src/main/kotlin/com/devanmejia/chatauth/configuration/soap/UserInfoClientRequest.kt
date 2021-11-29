package com.devanmejia.chatauth.configuration.soap

import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class UserInfoClientRequest <T>(var headerContent: String = "", var body: T? = null)
