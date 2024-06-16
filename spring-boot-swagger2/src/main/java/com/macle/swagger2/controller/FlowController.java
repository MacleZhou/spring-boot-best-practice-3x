package com.macle.swagger2.controller;

import com.macle.swagger2.annotations.FlowType;
import com.macle.swagger2.annotations.FlowTypes;
import com.macle.swagger2.controller.dto.FlowResult;
import com.macle.swagger2.controller.dto.FlowServiceParams1;
import com.macle.swagger2.controller.dto.FlowServiceParams2;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试Swagger改写", description = "测试Swagger改写注解")
@RequestMapping("/flow")
@RestController
public class FlowController {

    // 以下就是代表该接口支持发送两类流程服务
    @FlowTypes(name="发布服务", value = { //
            @FlowType(value = "流程服务1", paramType = FlowServiceParams1.class), //
            @FlowType(value = "流程服务2", paramType = FlowServiceParams2.class) //
    })
    @ApiOperation(value="createFlowService")
    @ResponseBody
    @PostMapping("/createFlowService")
    public FlowResult createFlowService() {
        return new FlowResult();
    }
}
