package cn.javastack.demoOrderDetail.v341.fetcher;

import cn.javastack.demoOrderDetail.vo.OrderDetailVO;
import cn.javastack.demoOrderDetail.vo.UserVO;
import cn.javastack.demoOrderDetail.vo.AddressVO;
import cn.javastack.demoOrderDetail.vo.ProductVO;
import cn.javastack.demoOrderDetail.vo.OrderVO;

public class OrderDetailVOFetcherV1 extends OrderDetailVO implements AddressVOFetcherV1, UserVOFetcherV1, ProductVOFetcherV1 {
    private UserVO userVO;

    private AddressVO addressVO;

    private ProductVO productVO;

    private OrderVO orderVO;

    public OrderDetailVOFetcherV1(OrderVO orderVO){
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
