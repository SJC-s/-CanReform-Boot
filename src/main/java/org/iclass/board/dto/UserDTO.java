package org.iclass.board.dto;

import lombok.*;
import org.iclass.board.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Long user_id;
    private String username;
    private String password;
    private String email;
    private String usersrole;
    private LocalDateTime created_at;
    private char is_active;



    /* JPA 구간 */
    public static UserDTO of(UserEntity entity) {
        return UserDTO.builder()
                .email(entity.getEmail())
                .is_active(entity.getIs_active())
                .user_id(entity.getUser_id())
                .usersrole(entity.getUsersrole())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .usersrole(this.usersrole)
                .is_active(this.is_active)
                .user_id(this.user_id)
                .password(this.password)
                .username(this.username)
                .build();
    }
}

