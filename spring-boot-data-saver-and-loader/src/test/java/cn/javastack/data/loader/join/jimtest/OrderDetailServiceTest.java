package cn.javastack.data.loader.join.jimtest;

import cn.javastack.data.loader.join.jimdemo.v1.OrderDetailServiceV1;
import cn.javastack.data.loader.join.jimdemo.v2.OrderDetailServiceV2;
import cn.javastack.data.loader.join.jimdemo.v3.OrderDetailServiceV3;
import cn.javastack.data.loader.join.jimdemo.v341.OrderDetailServiceFetcherV1;
import cn.javastack.data.loader.join.jimdemo.v342.OrderDetailServiceFetcherV2;
import cn.javastack.data.loader.join.jimdemo.v4.OrderDetailServiceV4;
import cn.javastack.data.loader.join.jimdemo.v4.OrderDetailServiceV41;
import cn.javastack.data.loader.join.jimdemo.v4.OrderDetailServiceV42;
import cn.javastack.data.loader.join.jimdemo.v5.OrderDetailServiceV5;
import cn.javastack.data.loader.join.jimdemo.v6.OrderDetailServiceV6;
import cn.javastack.data.loader.join.jimdemo.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class OrderDetailServiceTest {
    @Resource
    private OrderDetailServiceV1 orderDetailServiceV1;

    @Resource
    private OrderDetailServiceV2 orderDetailServiceV2;

    @Resource
    private OrderDetailServiceV3 orderDetailServiceV3;

    @Resource
    private OrderDetailServiceFetcherV1 orderDetailServiceFetcherV1;
    @Resource
    private OrderDetailServiceFetcherV2 orderDetailServiceFetcherV2;

    @Resource
    private OrderDetailServiceV4 orderDetailServiceV4;

    @Resource
    private OrderDetailServiceV41 orderDetailServiceV41;

    @Resource
    private OrderDetailServiceV42 orderDetailServiceV42;

    @Resource
    private OrderDetailServiceV5 orderDetailServiceV5;
    @Resource
    private OrderDetailServiceV6 orderDetailServiceV6;

    @Test
    public void getByUserIdV1_JuniorDeveloperWay(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV1.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV2_IntermediateDeveloperWay(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV2.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV3_SeniorDeveloperWay(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV3.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV341_FetcherStart(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV1.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV342_fetcherSenior(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV2.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV3422_fetcherSeniorConcurrent(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceFetcherV2.getByUserIdConcurrent(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV4_DataLoaderIoMemory_default_parallel(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV4.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV41_DataLoaderIoMemory_manual_serial(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV41.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    /**
     * 这个测试的目的是不用框架默认的ExecutorService,而用自己指定的ExecutorService。依赖配置DataLoaderTestConfig.class中配置的并发执行器
     * */
    @Test
    public void getByUserIdV42_DataLoaderIoMemory_manual_serial(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV42.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV5_DataLoaderIoMemory_SimplifiedByAliasAnnotation(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV5.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }

    @Test
    public void getByUserIdV6_DataLoaderIoMemory_Parallel(){
        List<? extends OrderDetailVO> orderDetailVOV1List = orderDetailServiceV6.getByUserId(1l);
        log.info(JSON.toJSONString(orderDetailVOV1List));
    }
}
