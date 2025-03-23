package cn.javastack.springboot.jpa.handler;

import cn.javastack.springboot.jpa.entity.UserDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userInResultHandler")
public class UserInResultHandler implements ResultHandler<List<UserDO>> {
    @Override
    public List<UserDO> process(List<Object> result) {
        if (result == null) {
            return null ;
        }
        return result.stream()
                // 这里我们知道返回的类型，所有可以直接进行类型的转换
                .flatMap(obj -> ((List<UserDO>)obj).stream())
                .collect(Collectors.toList()) ;
    }
}