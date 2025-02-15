package com.macle.study.easyrule.api;

import com.macle.study.easyrule.api.dto.BaseRuleRo;
import com.macle.study.easyrule.model.RuleResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rules")
public class RuleController {

    /*
    @PostMapping("/addRule")
    @ApiOperation("新增表达式规则")
    public ResponseResult<Long> addRule(@RequestBody BaseRuleRo ruleRo){
        return ruleService.addRule(ruleRo);
    }
    */

    /**
     * 匹配规则
     */
    /*
    @PostMapping("/match")
    @ApiOperation("匹配规则")
    public ResponseResult<List<RuleResult>> match(@RequestBody RuleMatchRo matchRo){
        return ruleService.match(matchRo);
    }
    */
}
