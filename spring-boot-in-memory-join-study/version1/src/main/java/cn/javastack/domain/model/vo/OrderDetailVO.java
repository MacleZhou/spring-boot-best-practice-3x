package cn.javastack.domain.model.vo;

// 为配合多种实现策略，使用抽象类进行统一
public abstract class OrderDetailVO {
    public abstract OrderVO getOrder();

    public abstract UserVO getUser();

    public abstract AddressVO getAddress();

    public abstract void setAddress(AddressVO addressVO);

    public abstract ProductVO getProduct();

    public abstract PayInfoVO getPayInfo();
}