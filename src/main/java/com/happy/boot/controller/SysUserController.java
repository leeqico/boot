package com.happy.boot.controller;

import com.happy.boot.entity.SysUser;
import com.happy.boot.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户表 前端控制器
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public List<SysUser> list(){
        List<SysUser> sysUsers = sysUserService.list();
        return sysUsers;
    }

}
