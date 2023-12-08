package cn.javastack.demoOrderDetail.v342.fetcher;

import cn.javastack.demoOrderDetail.vo.*;

public class OrderDetailVOFetcherV2 extends OrderDetailVO implements AddressVOFetcherV2, UserVOFetcherV2, ProductVOFetcherV2 {
    private UserVO userVO;

    private AddressVO addressVO;

    private ProductVO productVO;

    private OrderVO orderVO;

    public OrderDetailVOFetcherV2(OrderVO orderVO){
        this.orderVO = orderVO;
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
    public ProductVO getProduct() {
        return this.productVO;
    }


    @Override
    public Long getUserId() {
        return this.orderVO.getUserId();
    }
    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    @Override
    public Long getProductId() {
        return this.orderVO.getProductId();
    }

    public void setProduct(ProductVO productVO){
        this.productVO = productVO;
    }

    @Override
    public Long getAddressId() {
        return this.orderVO.getAddressId();
    }

    @Override
    public void setAddress(AddressVO addressVO) {
        this.addressVO = addressVO;
    }

}
