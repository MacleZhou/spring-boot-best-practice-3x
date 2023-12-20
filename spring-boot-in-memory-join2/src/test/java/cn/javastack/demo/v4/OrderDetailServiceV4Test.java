package cn.javastack.demo.v4;

import cn.javastack.jimdemo.v4.OrderDetailServiceV4;
import cn.javastack.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceV4Test {
    @Resource
    private OrderDetailServiceV4 orderDetailServiceV4;

    @Test
    public void getByUserId(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV4.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
