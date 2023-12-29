package cn.javastack.jimdemo.v342.fetcher;

import cn.javastack.jimdemo.v342.core.ItemFetcher;
import cn.javastack.jimdemo.vo.*;

public class OrderDetailVOFetcherV2 extends OrderDetailVO implements AddressVOFetcherV2, UserVOFetcherV2, ProductVOFetcherV2, ItemFetcher<OrderDetailVO> {
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

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    public void setProduct(ProductVO productVO){
        this.productVO = productVO;
    }

    @Override
    public void setAddress(AddressVO address) {
        this.addressVO = address;
    }

    @Override
    public void setResult(OrderDetailVO orderDetailVO) {

    }
}
