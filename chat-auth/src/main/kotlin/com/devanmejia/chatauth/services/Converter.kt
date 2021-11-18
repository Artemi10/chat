package com.devanmejia.chatauth.services

import com.devanmejia.chatauth.exceptions.ConverterException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.models.UserState
import io.spring.guides.gs_producing_web_service.State
import io.spring.guides.gs_producing_web_service.UserInfoDTO
import org.springframework.stereotype.Service
import java.util.*
import javax.xml.datatype.DatatypeConfigurationException
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

@Service
interface Converter<T, V>{
    fun convert(obj: V): T
    fun reconvert(obj: T): V
}

@Service
class UserConverter(
    private val dateConverter: Converter<XMLGregorianCalendar, Date>
): Converter<UserInfoDTO, User>{
    override fun convert(obj: User): UserInfoDTO {
        val userInfoDTO = UserInfoDTO()
        userInfoDTO.email = obj.email
        userInfoDTO.login = obj.login
        userInfoDTO.isAccountNonExpired = obj.isAccountNonExpired
        userInfoDTO.isAccountNonLocked = obj.isAccountNonLocked
        userInfoDTO.secretCode = obj.secretCode
        userInfoDTO.isEnable = obj.isEnabled
        userInfoDTO.password = obj.password
        userInfoDTO.state = State.valueOf(obj.state.name)
        userInfoDTO.isCredentialsNonExpired = obj.isCredentialsNonExpired
        userInfoDTO.birthDate = dateConverter.convert(obj.birthDate)
        return userInfoDTO
    }

    override fun reconvert(obj: UserInfoDTO): User {
        return User(
            obj.login, obj.password,
            dateConverter.reconvert(obj.birthDate),
            obj.email, obj.secretCode,
            UserState.valueOf(obj.state.name),
            obj.isEnable, obj.isAccountNonLocked,
            obj.isAccountNonExpired, obj.isCredentialsNonExpired
        )
    }
}

@Service
class DateConverter : Converter<XMLGregorianCalendar, Date> {

    override fun convert(obj: Date): XMLGregorianCalendar {
        val calendar = GregorianCalendar()
        calendar.time = obj
        return try {
            DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar)
        } catch (e: DatatypeConfigurationException) {
            throw ConverterException("Invalid date")
        }
    }

    override fun reconvert(obj: XMLGregorianCalendar): Date =
        obj.toGregorianCalendar().time

}
