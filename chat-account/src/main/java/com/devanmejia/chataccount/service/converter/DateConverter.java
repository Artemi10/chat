package com.devanmejia.chataccount.service.converter;

import com.devanmejia.chataccount.exception.ConverterException;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service("dateConverter")
public class DateConverter implements Converter<XMLGregorianCalendar, Date> {

    @Override
    public XMLGregorianCalendar convert(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try{
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e){
            throw new ConverterException("Invalid date");
        }
    }

    @Override
    public Date reconvert(XMLGregorianCalendar obj) {
        return obj.toGregorianCalendar().getTime();
    }
}
