package cn.javastack.demo.v342.fetcher;

import cn.javastack.demo.service.address.Address;
import cn.javastack.demo.service.address.AddressRepository;
import cn.javastack.demo.v342.core.BaseItemFetcherExecutor;
import cn.javastack.demo.vo.AddressVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AddressVOFetcherExecutorV2 extends BaseItemFetcherExecutor<OrderDetailVOFetcherV2, Address, AddressVO> {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    protected Long getFetchId(OrderDetailVOFetcherV2 fetcher){
        return fetcher.getOrder().getAddressId();
    }

    @Override
    protected List<Address> loadData(List<Long> ids){
        return this.addressRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(Address address){
        return address.getId();
    }

    @Override
    protected AddressVO convertToVo(Address address){
        return AddressVO.apply(address);
    }

    @Override
    protected void setResult(OrderDetailVOFetcherV2 fetcher, List<AddressVO> addressVOs){
        if (CollectionUtils.isNotEmpty(addressVOs)) {
            fetcher.setAddress(addressVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<OrderDetailVOFetcherV2> cls) {
        return OrderDetailVOFetcherV2.class.isAssignableFrom(cls);
    }
}