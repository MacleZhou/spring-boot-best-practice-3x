package cn.javastack.springboot.jpa.handler;

import java.util.List;

public interface ResultHandler<T> {
    T process(List<Object> result) ;
}