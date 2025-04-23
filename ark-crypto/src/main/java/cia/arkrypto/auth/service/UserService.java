package cia.arkrypto.auth.service;

import cia.arkrypto.auth.dao.UserRepository;
import cia.arkrypto.auth.security.CustomUserDetails;
import cia.arkrypto.auth.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public User findById(String id){
        User user = userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public Boolean matchPwd(String id, String pwd){
        User user = userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return user.getPwd().equals(pwd);
    }


    public User save(User user){
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 从数据库获取用户
        User user = userRepository.findById(id); // 你可以根据id来查找用户

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 这里你可以根据需要设置权限，暂时设置一个默认角色
        return new CustomUserDetails(user.getId(), user.getPwd(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
