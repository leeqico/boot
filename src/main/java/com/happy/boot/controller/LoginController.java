package com.happy.boot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.happy.boot.common.Result;
import com.happy.boot.common.CommonConstant;
import com.happy.boot.entity.SysUser;
import com.happy.boot.model.LoginModel;
import com.happy.boot.service.SysUserService;
import com.happy.boot.util.JwtUtil;
import com.happy.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("login")
    public Result<Object> login(LoginModel loginModel){
        if (loginModel == null || StrUtil.isBlank(loginModel.getUsername()) || StrUtil.isBlank(loginModel.getPassword()) ){
            return Result.fail("请输入用户名或密码");
        }
        Result<Object> result = new Result<>();
        SysUser sysUser = sysUserService.getUserByName(loginModel.getUsername());
        if (sysUser == null){
            return Result.fail("用户不存在");
        } else {
            if (!StrUtil.equals(sysUser.getPassword(),loginModel.getPassword())){
                return Result.fail("用户名或密码错误");
            }
            //生成token
            String token = JwtUtil.sign(sysUser.getUsername(),sysUser.getPassword());
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
            //设置超时时间
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
            JSONObject object = new JSONObject();
            object.put("token",token);
            object.put("userInfo",sysUser);
            result.setData(object);
            result.setMessage("登录成功");
            log.info("用户：{}，登录成功！",sysUser.getUsername());
        }
        return result;
    }

}
