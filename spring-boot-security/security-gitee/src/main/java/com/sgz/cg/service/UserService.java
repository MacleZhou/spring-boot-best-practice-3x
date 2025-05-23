package com.sgz.cg.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sgz.cg.entry.Permission;
import com.sgz.cg.entry.RolePermission;
import com.sgz.cg.entry.User;
import com.sgz.cg.entry.UserRole;
import com.sgz.cg.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private UserMapper mapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;

    public List<Permission> getPermissionByUsername(String username) {
        User user = super.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username), true);
        return this.getPermissionByUser(user);
    }

    public List<Permission> getPermissionByUserId(Integer userId) {
        User user = super.getById(userId);
        return this.getPermissionByUser(user);
    }

    public List<Permission> getPermissionByUser(User user) {
        List<Permission> permissions = new ArrayList<>();
        if (null != user) {
            List<UserRole> userRoles = userRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getId()));
            if (CollectionUtils.isNotEmpty(userRoles)) {
                List<Integer> roleIds = new ArrayList<>();
                userRoles.stream().forEach(userRole -> {
                    roleIds.add(userRole.getRoleId());
                });
                List<RolePermission> rolePermissions = rolePermissionService.list(Wrappers.<RolePermission>lambdaQuery().in(RolePermission::getRoleId, roleIds));
                if (CollectionUtils.isNotEmpty(rolePermissions)) {
                    List<Integer> permissionIds = new ArrayList<>();
                    rolePermissions.stream().forEach(rolePermission -> {
                        permissionIds.add(rolePermission.getPermissionId());
                    });
                    permissions = permissionService.list(Wrappers.<Permission>lambdaQuery().in(Permission::getId, permissionIds));
                }
            }
        }
        return permissions;
    }

}