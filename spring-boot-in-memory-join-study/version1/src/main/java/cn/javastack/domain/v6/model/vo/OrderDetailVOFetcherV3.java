package cn.javastack.domain.v6.model.vo;

import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v6.fetcher.ItemFetcher;

public class OrderDetailVOFetcherV3 extends OrderDetailVO implements ItemFetcher<OrderDetailVO> {
    private UserVO userVO;

    private AddressVO addressVO;

    private ProductVO productVO;

    private PayInfoVO payInfo;

    private OrderVO orderVO;

    public OrderDetailVOFetcherV3(OrderVO orderVO){
        this.orderVO = orderVO;
    }

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }


    public void setProduct(ProductVO productVO){
        this.productVO = productVO;
    }


    @Override
    public OrderVO getOrder() {
        return null;
    }

    @Override
    public UserVO getUser() {
        return null;
    }

    @Override
    public AddressVO getAddress() {
        return null;
    }

    @Override
    public void setAddress(AddressVO addressVO) {
        this.addressVO = addressVO;
    }

    @Override
    public ProductVO getProduct() {
        return this.productVO;
    }

    @Override
    public PayInfoVO getPayInfo() {
        return this.payInfo;
    }

    @Override
    public Long getFetchId() {
        return this.orderVO.getId();
    }

    @Override
    public void setResult(OrderDetailVO orderDetailVO) {

    }
}
