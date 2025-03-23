package cn.javastack.springboot.jpa.repo;

import cn.javastack.springboot.jpa.entity.UserDO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserDO, Long> {
    List<UserDO> findByUsername(String username, List<Long> ids);
}

