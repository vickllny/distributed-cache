package com.vickllny.distributedcache.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class UserRoleDTO {

    private User user;
    private Role role;

    public UserRoleDTO(final User user, final Role role) {
        this.user = user;
        this.role = role;
    }

    public UserRoleDTO() {
    }

    public static List<UserRoleDTO> parse(final List<User> userList, final List<Role> roleList){
        final List<UserRoleDTO> list = new LinkedList<>();
        int size = Math.min(userList.size(), roleList.size());
        for (int i = 0; i < size; i++) {
            list.add(new UserRoleDTO(userList.get(i), roleList.get(i)));
        }
        return list;
    }

}
