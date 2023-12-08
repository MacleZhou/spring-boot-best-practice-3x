package cn.javastack.domain.v6.model.vo;


import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v6.fetcher.ItemFetcher;

public class OrderListVOFetcherV3 extends OrderListVO implements ItemFetcher<OrderListVO> {
    private OrderVO orderVO;

    private AddressVO addressVO;

    private UserVO userVO;

    private ProductVO productVO;

    public OrderListVOFetcherV3(OrderVO orderVO){
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

    public void setAddress(AddressVO addressVO){
        this.addressVO = addressVO;
    }

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    public void setProduct(ProductVO productVO) {
        this.productVO = productVO;
    }

    @Override
    public Long getFetchId() {
        return orderVO.getId();
    }

    @Override
    public void setResult(OrderListVO orderListVO) {

    }
}
