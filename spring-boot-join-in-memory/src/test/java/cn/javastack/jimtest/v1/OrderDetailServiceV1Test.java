package cn.javastack.jimtest.v1;


import cn.javastack.jimdemo.v1.OrderDetailServiceV1;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceV1Test {

    @Resource
    private OrderDetailServiceV1 orderDetailServiceV1;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV1.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
