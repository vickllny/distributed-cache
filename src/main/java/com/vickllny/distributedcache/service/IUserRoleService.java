package com.vickllny.distributedcache.service;

import com.vickllny.distributedcache.domain.UserRoleDTO;

import java.util.List;

public interface IUserRoleService {

    boolean save(final UserRoleDTO userRole);

    List<UserRoleDTO> listUserRole();
}
