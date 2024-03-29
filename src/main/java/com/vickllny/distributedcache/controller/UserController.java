package com.vickllny.distributedcache.controller;

import com.vickllny.distributedcache.domain.User;
import com.vickllny.distributedcache.domain.UserRoleDTO;
import com.vickllny.distributedcache.service.IUserRoleService;
import com.vickllny.distributedcache.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequestMapping("/user")
@RestController
public class UserController {


    @Autowired
    private IUserService iUserService;
    @Autowired
    private IUserRoleService iUserRoleService;


    @GetMapping("/list")
    public List<User> userList(){
        return iUserService.list();
    }

    @GetMapping("/list-user-role")
    public List<UserRoleDTO> listUserRole(){
        return iUserRoleService.listUserRole();
    }

    @PostMapping("/add")
    public Boolean add(@RequestBody @Valid User user){
        return iUserService.saveUser(user);
    }

    @PostMapping("/add-user-role")
    public Boolean addUserRole(@RequestBody @Valid UserRoleDTO user){
        return iUserRoleService.save(user);
    }


}
