package com.happy.boot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.boot.common.Result;
import com.happy.boot.entity.SysUser;
import com.happy.boot.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public Result list(Page page){
        log.info("查询开始！");
        IPage<SysUser> sysUsers = sysUserService.list(page);
        log.info("查询成功！");
        return Result.success(sysUsers);
    }

}
