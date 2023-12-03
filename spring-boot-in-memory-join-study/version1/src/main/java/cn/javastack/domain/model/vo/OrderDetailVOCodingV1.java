package cn.javastack.domain.model.vo;

public class OrderDetailVOCodingV1 extends OrderDetailVO{
    private UserVO userVO;

    private ProductVO productVO;

    private PayInfoVO payInfo;

    private OrderVO orderVO;

    public OrderDetailVOCodingV1(OrderVO orderVO){
        this.orderVO = orderVO;
    }

    public void setUser(UserVO userVO) {
        this.userVO = userVO;
    }

    public void setProduct(ProductVO productVO){
        this.productVO = productVO;
    }

    public void setPayInfo(PayInfoVO payInfo) {
        this.payInfo = payInfo;
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

    }

    @Override
    public ProductVO getProduct() {
        return null;
    }

    @Override
    public PayInfoVO getPayInfo() {
        return this.payInfo;
    }
}
