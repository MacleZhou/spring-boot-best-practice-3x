package com.macle.study.anti.shaking.api.dto;

import java.util.List;

import com.macle.study.anti.shaking.annotation.RequestKeyParam;
import lombok.Data;

@Data
public class AddReq {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    @RequestKeyParam
    private String userPhone;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;
}
