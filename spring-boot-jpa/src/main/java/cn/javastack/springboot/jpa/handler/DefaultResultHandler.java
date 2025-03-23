package cn.javastack.springboot.jpa.handler;

import java.util.List;

public class DefaultResultHandler implements ResultHandler<Object> {
    @Override
    public Object process(List<Object> result) {
        return result ;
    }
}