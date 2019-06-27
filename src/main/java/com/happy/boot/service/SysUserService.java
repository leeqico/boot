package com.happy.boot.service;

import com.happy.boot.entity.SysUser;

import java.util.List;

/**
 * 用户表 服务类
 */
public interface SysUserService {

    List<SysUser> list();

    SysUser getUserByName(String username);
}
