package com.atshuo.audit.dataPermission;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 类 DataScopeMapper
 *
 * @author ChenQi
 * @date 2023/3/28
 */
@Mapper
@Component
public interface DataScopeMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    @InterceptorIgnore
    T selectById(Serializable id);

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    @Override
    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    @Override
    <P extends IPage<T>> P selectPage(P page, @Param(
            Constants.WRAPPER) Wrapper<T> queryWrapper);
}