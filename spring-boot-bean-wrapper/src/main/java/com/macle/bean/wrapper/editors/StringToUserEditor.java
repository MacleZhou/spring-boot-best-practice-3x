package com.macle.bean.wrapper.editors;


import com.macle.bean.wrapper.pojo.User;

import java.beans.PropertyEditorSupport;

public class StringToUserEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        User user = new User();
        String[] infos = text.split(",");
        user.setAge(Integer.parseInt(infos[0]));
        user.setName(infos[1]);
        setValue(user);
    }
}