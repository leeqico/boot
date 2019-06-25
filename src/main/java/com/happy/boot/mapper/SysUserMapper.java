package com.happy.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.boot.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
