package cn.javastack.domain.v4.model.vo;


import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v4.fetcher.AddressVOFetcherV1;
import cn.javastack.domain.v4.fetcher.ProductVOFetcherV1;
import cn.javastack.domain.v4.fetcher.UserVOFetcherV1;

public class OrderListVOFetcherV1 extends OrderListVO
        implements AddressVOFetcherV1,
        ProductVOFetcherV1,
        UserVOFetcherV1 {
    private OrderVO orderVO;

    private AddressVO addressVO;

    private UserVO userVO;

    private ProductVO productVO;

    public OrderListVOFetcherV1(OrderVO orderVO){
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
    public Long getAddressId() {
        return this.addressVO.getId();
    }

    public void setAddress(AddressVO addressVO){
        this.addressVO = addressVO;
    }

    @Override
    public Long getUserId() {
        return this.userVO.getId();
    }

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    @Override
    public Long getProductId() {
        return this.productVO.getId();
    }

    public void setProduct(ProductVO productVO) {
        this.productVO = productVO;
    }
}
