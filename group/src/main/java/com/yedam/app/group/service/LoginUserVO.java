package com.yedam.app.group.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yedam.app.group.service.EmpVO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginUserVO implements UserDetails {
    
    private final EmpVO empVO; // EmpVO 기반으로 변경
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(empVO.getRightsName())); // 권한명 설정
        return auths;
    }

    @Override
    public String getPassword() {
        return empVO.getEmployeePw();
    }

    @Override
    public String getUsername() {
        return empVO.getEmployeeId();
    }

    @Override
    public boolean isAccountNonExpired() { // 계정 만료 여부
        return !"Y".equals(empVO.getResignationStatus()); // 퇴사 여부 체크
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠금 여부
        return !"Y".equals(empVO.getResignationStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정 사용 여부
        return !"Y".equals(empVO.getResignationStatus()); // 퇴사한 계정은 비활성화
    }
}
