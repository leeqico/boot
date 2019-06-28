package com.happy.boot.shiro;

import cn.hutool.core.util.StrUtil;
import com.happy.boot.common.constant.CommonConstant;
import com.happy.boot.entity.SysUser;
import com.happy.boot.service.SysUserService;
import com.happy.boot.util.JwtUtil;
import com.happy.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户登录鉴权和获取用户授权
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    private SysUserService sysUserService;

    /**
     * 必须重写该方法，不然Shiro报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 功能： 获取用户权限信息，包括角色以及权限。只有当触发检测用户权限时才会调用此方法，例如checkRole,checkPermission
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("————权限认证 [ roles、permissions]————");
        SysUser sysUser = null;
        String username =null;
        if (principalCollection != null){
            sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
            username =sysUser.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 设置用户拥有的角色集合，比如“admin,test”,这里后续通过用户名查数据库
        Set<String> roleSet = new HashSet<>();
        roleSet.add("admin");
        info.setRoles(roleSet);

        // 设置用户拥有的权限集合，比如“sys:role:add,sys:user:add”,这里后续通过用户名查数据库
        Set<String> permissionSet = new HashSet<>();
        permissionSet.add("sys:user:list");
        info.addStringPermissions(permissionSet);

        return info;
    }

    /**
     * 功能： 用来进行身份认证，也就是说验证用户输入的账号和密码是否正确，获取身份验证信息，错误抛出异常
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————身份认证————");
        String token = (String) authenticationToken.getCredentials();
        if (token == null){
            throw new AuthenticationException("token为空");
        }
        SysUser sysUser = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }

    /**
     * 检验token的有效性
     * @param token
     * @return
     * @throws AuthenticationException
     */
    private SysUser checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得userName，用于和数据库进行比对
        String username = JwtUtil.getUsername(token);
        if (username == null){
            throw new AuthenticationException("token非法无效！");
        }
        // 查询用户信息
        SysUser sysUser = sysUserService.getUserByName(username);
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在!");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, sysUser.getPassword())) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }

        return sysUser;
    }

    /**
     * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
     * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
     * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
     * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
     * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
     * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
     * 参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
     *
     * @param username
     * @param password
     * @return
     */
    public boolean jwtTokenRefresh(String token, String username, String password) {
        String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (StrUtil.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(token, username, password)) {
                String newAuthorization = JwtUtil.sign(username, password);
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            } else {
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, cacheToken);
                // 设置超时时间
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            }
            return true;
        }
        return false;
    }
}
