package org.iclass.board.dto;

import lombok.*;
import org.iclass.board.entity.LoginEntity;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Date;

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




    public static UserDTO loginToDTO(LoginEntity entity) {
        return UserDTO.builder()
                    .email(entity.getEmail())
                    .is_active(entity.getIs_active())
                    .user_id(entity.getUserid())
                    .usersrole(entity.getUsersrole())
                    .password(entity.getPassword())
                    .username(entity.getUsername())
                    .build();
    }

    public LoginEntity loginToEntity(){
        return (
                LoginEntity.builder()
                        .email(this.email)
                        .usersrole(this.usersrole)
                        .is_active(this.is_active)
                        .userid(this.user_id)
                        .password(this.password)
                        .username(this.username)
                        .build()
        );
    }


    /* JPA 구간 */
    public static UserDTO of(UserEntity entity) {
        return UserDTO.builder()
                .email(entity.getEmail())
                .is_active(entity.getIs_active())
                .user_id(entity.getUser_id())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .user_id(user_id)
                .username(username)
                .email(email)
                .build();
    }
}

