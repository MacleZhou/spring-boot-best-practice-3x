package cn.javastack.springboot.jpa.service;

import cn.javastack.springboot.jpa.annotation.BatchParam;
import cn.javastack.springboot.jpa.annotation.SplitQuery;
import cn.javastack.springboot.jpa.entity.UserDO;
import cn.javastack.springboot.jpa.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SplitQuery(batchSize = 2, handlerName = "userInResultHandler")
    public List<UserDO> queryPersona(String name, @BatchParam List<Long> ids) {
        return this.userRepository.findByUsername(name, ids);
    }
}
