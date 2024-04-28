package com.macle.bean.wrapper.pojo;


import java.beans.PropertyEditorSupport;

public class User2Editor extends PropertyEditorSupport {

    public void setAsText(String text) {
        User2 user = new User2();
        String[] infos = text.split(",");
        user.setAge(Integer.parseInt(infos[0]));
        user.setName(infos[1]);
        setValue(user);
    }
}