package cn.javastack.domain.model.vo;

import cn.javastack.domain.model.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVO {
    private Long id;
    private String name;
    private UserVO(){}
    public static UserVO apply(User user){
        return new UserVO();
    }
}
