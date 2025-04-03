package cia.northboat.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private String id;
    private String pwd;
    private Collection<? extends GrantedAuthority> authorities;

    // 构造函数
    public CustomUserDetails(String id, String pwd, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.pwd = pwd;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账号是否过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账号是否被锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 密码是否过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 账号是否可用
    }
}
