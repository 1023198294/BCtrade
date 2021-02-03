package com.example.demo.shiro;

import com.example.demo.dao.UserMapper;
import com.example.demo.service.RegisterService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.demo.model.User;

public class LoginAuthorizingRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) getAvailablePrincipal(principals);
        RegisterService registerService = new RegisterService();
        User user = null;
        try {
            user = registerService.findUserByName(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> roles = new HashSet<>();
        assert user != null;
        roles.add(user.getRole());
        //roles.add("super");
        //roles.add("admin");
        //roles.add("user");
        Set<String> permissions = new HashSet<>();
        //permissions.add("admin:shiro:list");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        /*
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("admin","admin");
        userInfo.put("user1","123456");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        if(username.isEmpty()){
            throw new AccountException("用户名不能为空");
        }
        //Exist？
        String password = userInfo.get(username);
        if(password==null){
            throw new UnknownAccountException("用户不存在");
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username,password,"admin");

        return simpleAuthenticationInfo;
        */
        String resource = "sqlMapConfig.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        if(username.isEmpty()){
            throw new AccountException("用户名不能为空");
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.findUserByName(usernamePasswordToken.getUsername());
        if(user == null){
                throw new UnknownAccountException("用户不存在");
        }

        return new SimpleAuthenticationInfo(username,user.getPassword(),user.getRole());
    }
}
