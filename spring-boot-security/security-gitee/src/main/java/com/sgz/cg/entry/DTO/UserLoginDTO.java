package com.sgz.cg.entry.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    private String username;
    private String password;
}