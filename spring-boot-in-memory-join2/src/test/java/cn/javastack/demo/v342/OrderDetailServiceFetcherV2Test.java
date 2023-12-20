package cn.javastack.demo.v342;

import cn.javastack.jimdemo.v342.OrderDetailServiceFetcherV2;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceFetcherV2Test {
    @Resource
    private OrderDetailServiceFetcherV2 orderDetailServiceFetcherV2;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV2.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdConcurrent(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV2.getByUserIdConcurrent(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
