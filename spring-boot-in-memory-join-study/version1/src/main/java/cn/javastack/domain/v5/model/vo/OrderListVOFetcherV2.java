package cn.javastack.domain.v5.model.vo;


import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v5.fetcher.AddressVOFetcherV2;
import cn.javastack.domain.v5.fetcher.ProductVOFetcherV2;
import cn.javastack.domain.v5.fetcher.UserVOFetcherV2;

public class OrderListVOFetcherV2 extends OrderListVO implements AddressVOFetcherV2, UserVOFetcherV2, ProductVOFetcherV2 {
    private OrderVO orderVO;

    private AddressVO addressVO;

    private UserVO userVO;

    private ProductVO productVO;

    public OrderListVOFetcherV2(OrderVO orderVO){
        this.orderVO = orderVO;
    }

    public OrderVO getOrder(){
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
    public Long getFetchId() {
        return this.orderVO.getId();
    }

    @Override
    public void setUser(UserVO user) {

    }

    @Override
    public Long getAddressId() {
        return this.orderVO.getAddressId();
    }

    @Override
    public void setAddress(AddressVO address) {
        this.setAddress(address);
    }

    @Override
    public void setResult(AddressVO addressVO) {
        this.setAddress(addressVO);
    }

    @Override
    public Long getUserId() {
        return this.orderVO.getUserId();
    }

    @Override
    public void setResult(UserVO userVO) {
        UserVOFetcherV2.super.setResult(userVO);
    }

    @Override
    public Long getProductId() {
        return this.orderVO.getProductId();
    }

    @Override
    public void setProduct(ProductVO product) {
        this.setResult(product);
    }

    @Override
    public void setResult(ProductVO productVO) {
        this.setProduct(productVO);
    }

}
