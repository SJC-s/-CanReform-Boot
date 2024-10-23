package org.iclass.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class UserAccountDTO extends User{

    public UserAccountDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}

/*
// 스프링 시큐리티가 관리하는 사용자 인증 정보 DTO
// 새로운 필드를 추가 하려면 상속이 아닌 구현을 써야한다
public class UserAccountDTO implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // 예상 시나리오 : 소셜 인증을 추가한다면 소셜 인증 여부를 저장
    private boolean social;
    // 이 외에도 필요한 사용자 정보를 추가로 저장하는 User 자식 클래스

    // Collection<? extends GrantedAuthority>
    // - Collection 의 제너릭 타입 ? 은 GrantedAuthority 를 상속받은 타입이어야 한다
    // - 권한-role 은 여러가지 값을 가질 수 있도록 Collection-List, Map, Set 타입)
    public UserAccountDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.social = false;
    }
}*/
