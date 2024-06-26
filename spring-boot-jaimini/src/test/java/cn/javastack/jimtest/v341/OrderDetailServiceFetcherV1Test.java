package cn.javastack.jimtest.v341;

import cn.javastack.jimdemo.v341.OrderDetailServiceFetcherV1;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceFetcherV1Test {
    @Resource
    private OrderDetailServiceFetcherV1 orderDetailServiceFetcherV1;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV1.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
