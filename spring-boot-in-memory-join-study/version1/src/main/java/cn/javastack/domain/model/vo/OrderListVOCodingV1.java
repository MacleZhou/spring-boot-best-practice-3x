package cn.javastack.domain.model.vo;


public class OrderListVOCodingV1 extends OrderListVO{
    private OrderVO orderVO;

    private AddressVO addressVO;

    private UserVO userVO;

    private ProductVO productVO;

    public OrderListVOCodingV1(OrderVO orderVO){
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
}
