package com.sofang.service.impl;

import com.sofang.entity.Role;
import com.sofang.entity.User;
import com.sofang.repository.RoleRepository;
import com.sofang.repository.UserRepository;
import com.sofang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findUserByName(String username) {
        User user = userRepository.findByName(username);

        if(user == null){
            return null;
        }
        List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        if(roles == null || roles.isEmpty()){
            throw new DisabledException("权限非法");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }
}
