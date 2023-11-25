package com.vickllny.distributedcache.service.impl;

import com.vickllny.distributedcache.domain.Role;
import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.domain.UserRoleDTO;
import com.vickllny.distributedcache.service.IRoleService;
import com.vickllny.distributedcache.service.IUserRoleService;
import com.vickllny.distributedcache.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IUserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;

    @Override
    @Transactional
    public boolean save(final UserRoleDTO userRole) {
        iUserService.saveUser(userRole.getUser());
        iRoleService.saveRole(userRole.getRole());
        return true;
    }

    @Override
    public List<UserRoleDTO> listUserRole() {
        final List<User> userList = iUserService.list();
        final List<Role> roleList = iRoleService.list();
        return UserRoleDTO.parse(userList, roleList);
    }
}
