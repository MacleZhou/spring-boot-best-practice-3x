package cn.javastack.demo.v3;

import cn.javastack.demoOrderDetail.v3.OrderDetailServiceV3;
import cn.javastack.demoOrderDetail.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceV3Test {
    @Resource
    private OrderDetailServiceV3 orderDetailServiceV3;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV3.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
