package cn.javastack.domain.v5.model.vo;


import cn.javastack.domain.model.entity.Address;
import cn.javastack.domain.model.entity.Order;
import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v4.fetcher.AddressVOFetcherV1;
import cn.javastack.domain.v4.fetcher.ProductVOFetcherV1;
import cn.javastack.domain.v4.fetcher.UserVOFetcherV1;
import cn.javastack.domain.v5.fetcher.AddressVOFetcherV2;
import cn.javastack.domain.v5.fetcher.BaseItemFetcherExecutor;

import java.util.List;

public class OrderListVOFetcherV2 extends BaseItemFetcherExecutor<OrderListVO, Order, OrderVO> {

    @Override
    protected Long getFetchId(OrderListVO fetcher) {
        return fetcher.getOrder().getId();
    }

    @Override
    protected List<Order> loadData(List<Long> ids) {
        return null;
    }

    @Override
    protected Long getDataId(Order order) {
        return null;
    }

    @Override
    protected OrderVO convertToVo(Order order) {
        return null;
    }

    @Override
    protected void setResult(OrderListVO fetcher, List<OrderVO> result) {

    }

    @Override
    public boolean support(Class<OrderListVO> cls) {
        return false;
    }
}
