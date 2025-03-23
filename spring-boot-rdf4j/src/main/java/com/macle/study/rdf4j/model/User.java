package com.macle.study.rdf4j.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class User {
    private String uid; // Unique identifier for the user
    private String name;
    private String email;

    public boolean isValid() {
        return StringUtils.isNotBlank(name) && StringUtils.isNotBlank(email);
    }
}