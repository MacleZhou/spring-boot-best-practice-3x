package cn.javastack.demo.v341.fetcher;

import cn.javastack.demo.service.address.Address;
import cn.javastack.demo.service.address.AddressRepository;
import cn.javastack.demo.vo.AddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class AddressVOFetcherExecutorV1 {
    @Autowired
    private AddressRepository addressRepository;

    public void fetch(List<? extends AddressVOFetcherV1> fetchers){
        List<Long> ids = fetchers.stream()
                .map(AddressVOFetcherV1::getAddressId)
                .distinct()
                .collect(Collectors.toList());

        List<Address> addresses = addressRepository.getByIds(ids);

        Map<Long, Address> addressMap = addresses.stream()
                .collect(toMap(address -> address.getId(), Function.identity()));

        fetchers.forEach(fetcher -> {
            Long addressId = fetcher.getAddressId();
            Address address = addressMap.get(addressId);
            if (address != null){
                AddressVO addressVO = AddressVO.apply(address);
                fetcher.setAddress(addressVO);
            }
        });
    }
}