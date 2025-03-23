package cn.javastack.springboot.jpa.controller;

import cn.javastack.springboot.jpa.entity.UserDO;
import cn.javastack.springboot.jpa.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/info/{id}")
    public UserDO getUserInfo(@PathVariable("id") long id){
        UserDO userDO = userRepository.findById(id).orElseGet(null);
        return userDO;
    }

    @GetMapping("/query")
    public List<UserDO> query(){
        return this.userRepository.findByUsername("name", List.of(1L, 2L, 3L, 4L, 5L));
    }
}