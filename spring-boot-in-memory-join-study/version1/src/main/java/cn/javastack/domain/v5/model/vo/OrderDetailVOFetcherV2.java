package cn.javastack.domain.v5.model.vo;

import cn.javastack.domain.model.vo.*;
import cn.javastack.domain.v5.fetcher.ItemFetcher;

public class OrderDetailVOFetcherV2 extends OrderDetailVO implements ItemFetcher<OrderDetailVO>{
    private UserVO userVO;

    private AddressVO addressVO;

    private ProductVO productVO;

    private PayInfoVO payInfo;

    private OrderVO orderVO;

    public OrderDetailVOFetcherV2(OrderVO orderVO){
        this.orderVO = orderVO;
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
        return null;
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
        public Long getFetchId() {
            return getOrder().getId();
        }

        @Override
        public void setResult(OrderDetailVO address) {

        }

} {
}
