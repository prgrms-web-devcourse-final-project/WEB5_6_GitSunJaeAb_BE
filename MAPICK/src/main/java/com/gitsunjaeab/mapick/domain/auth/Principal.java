package com.gitsunjaeab.mapick.domain.auth;

import com.gitsunjaeab.mapick.domain.member.Member;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class Principal extends User { // Spring Security의 User 객체를 확장해서 커스터 마이징한 사용자 정보 객체
// Spring Security 내부에서 사용하는 인증 객체를 내 도메인에 맞게 확장해서 인증된 사용자에 대한 더 풍부한 정보를 다루기 위해 사용하는 클래스

    private final Member member;

    public Principal(Member member) {
        super(
                member.getId().toString(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getRole()))
        );
        this.member = member;
    }

    // UserDetails 객체 만들기
    private Principal(String username, String password, Collection<? extends GrantedAuthority> authorities, Member member) {
        super(username, password, authorities);
        this.member = member;
    }

    public static Principal createPrincipal(Member member, List<SimpleGrantedAuthority> authorities){
        return new Principal(
                member.getId().toString(),
                member.getPassword(),
                authorities,
                member
        );
    }
}