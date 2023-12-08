package cn.javastack.domain.v6.fetcher;

import cn.javastack.domain.model.entity.Address;
import cn.javastack.domain.model.vo.AddressVO;
import cn.javastack.domain.repository.AddressRepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressVOFetcherExecutorV3
        extends BaseItemFetcherExecutor<AddressVOFetcherV3, Address, AddressVO> {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    protected Long getFetchId(AddressVOFetcherV3 fetcher) {
        return fetcher.getAddressId();
    }

    @Override
    protected List<Address> loadData(List<Long> ids) {
        return this.addressRepository.getByIds(ids);
    }

    @Override
    protected Long getDataId(Address address) {
        return address.getId();
    }

    @Override
    protected AddressVO convertToVo(Address address) {
        return AddressVO.apply(address);
    }

    @Override
    protected void setResult(AddressVOFetcherV3 fetcher, List<AddressVO> addressVOs) {
        if (CollectionUtils.isNotEmpty(addressVOs)) {
            fetcher.setAddress(addressVOs.get(0));
        }
    }

    @Override
    public boolean support(Class<AddressVOFetcherV3> cls) {
        // 暂时忽略，稍后会细讲
        return AddressVOFetcherV3.class.isAssignableFrom(cls);
    }
}