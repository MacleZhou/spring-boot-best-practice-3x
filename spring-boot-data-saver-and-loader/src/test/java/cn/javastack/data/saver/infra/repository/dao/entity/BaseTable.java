package cn.javastack.data.saver.infra.repository.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class BaseTable<ID> {
    @TableId
    private ID id;
}
