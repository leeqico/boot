package com.happy.boot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.boot.entity.SysUser;

import java.util.List;

/**
 * 用户表 服务类
 */
public interface SysUserService {

    IPage<SysUser> list(Page page);

    SysUser getUserByName(String username);
}
