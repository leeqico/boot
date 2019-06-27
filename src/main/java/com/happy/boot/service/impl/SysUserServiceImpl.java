package com.happy.boot.service.impl;

import com.happy.boot.entity.SysUser;
import com.happy.boot.mapper.SysUserMapper;
import com.happy.boot.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> list() {
        return sysUserMapper.selectList(null);
    }

    @Override
    public SysUser getUserByName(String username) {
        return sysUserMapper.getUserByName(username);
    }

}
