package cn.javastack.demo.v5;

import cn.javastack.demo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceV5Test {
    @Resource
    private OrderDetailServiceV5 orderDetailServiceV5;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV5.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
