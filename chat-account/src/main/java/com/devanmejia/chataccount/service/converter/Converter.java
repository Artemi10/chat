package com.devanmejia.chataccount.service.converter;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface Converter<T, V> {
    T convert(V obj);
    default List<T> convert(Collection<V> objs){
        return objs.stream().map(this::convert).collect(Collectors.toList());
    }
}
