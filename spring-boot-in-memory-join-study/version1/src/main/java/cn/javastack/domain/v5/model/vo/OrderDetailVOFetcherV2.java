package cn.javastack.domain.v5.model.vo;

import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v5.fetcher.*;

public class OrderDetailVOFetcherV2 extends OrderDetailVO implements UserVOFetcherV2, AddressVOFetcherV2, ProductVOFetcherV2, PayInfoVOFetcherV2 {
    private UserVO userVO;

    private AddressVO addressVO;

    private ProductVO productVO;

    private PayInfoVO payInfo;

    private OrderVO orderVO;

    public OrderDetailVOFetcherV2(OrderVO orderVO){
        this.orderVO = orderVO;
    }

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    @Override
    public void setResult(UserVO userVO) {
        UserVOFetcherV2.super.setResult(userVO);
    }


    @Override
    public Long getProductId() {
        return null;
    }

    public void setProduct(ProductVO productVO){
        this.productVO = productVO;
    }


    @Override
    public OrderVO getOrder() {
        return this.orderVO;
    }

    @Override
    public UserVO getUser() {
        return this.userVO;
    }

    @Override
    public AddressVO getAddress() {
        return this.addressVO;
    }

    @Override
    public Long getAddressId() {
        return this.addressVO.getId();
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
    public Long getPayInfoId() {
        return this.payInfo.getId();
    }

    @Override
    public void setPayInfo(PayInfoVO payInfo) {

    }

    @Override
    public Long getUserId() {
        return null;
    }

    @Override
    public Long getFetchId() {
        return this.orderVO.getId();
    }

    @Override
    public void setResult(ProductVO productVO) {
        ProductVOFetcherV2.super.setResult(productVO);
    }

    @Override
    public void setResult(PayInfoVO payInfoVO) {
        PayInfoVOFetcherV2.super.setResult(payInfoVO);
    }

    @Override
    public void setResult(AddressVO addressVO) {
        AddressVOFetcherV2.super.setResult(addressVO);
    }
}
