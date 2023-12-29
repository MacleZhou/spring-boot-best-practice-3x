package cn.javastack.data.loader.join.jimtest.v2;

import cn.javastack.data.loader.join.jimdemo.v2.OrderDetailServiceV2;
import cn.javastack.data.loader.join.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceV2Test {
    @Resource
    private OrderDetailServiceV2 orderDetailServiceV2;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV2.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
