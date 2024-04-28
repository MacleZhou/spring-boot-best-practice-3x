package com.macle.bean.wrapper.converts;

import com.macle.bean.wrapper.pojo.User;
import org.springframework.core.convert.converter.Converter;

public class StringToUserConvert implements Converter<String, User> {

    @Override
    public User convert(String source) {
        User user = new User() ;
        String[] infos = source.split(",") ;
        user.setAge(Integer.parseInt(infos[0])) ;
        // 这里为了与上面的StringToUserEditor区分，故意加了 ' -'后缀
        user.setName(infos[1] + " -") ;
        return user ;
    }
}