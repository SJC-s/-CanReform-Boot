package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.UserAccountDTO;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    public final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("Response Email : {}", userId);
        UserEntity entity = userRepository.findByUserId(userId);
        log.info("Response Entity : {}", entity);
        if(entity == null){
            throw new UsernameNotFoundException(userId);
        }

        UserDTO dto = UserDTO.of(entity);
        log.info(dto.toString());

        UserDetails userDetails = UserAccountDTO.builder()
                .username(dto.getUserId())
                .password(dto.getPassword())
                .authorities(new SimpleGrantedAuthority(dto.getUsersRole()))
                .build();
        log.info("CustomUserDetailsService.userDetails : {}", userDetails);
        return userDetails;
    }
}
