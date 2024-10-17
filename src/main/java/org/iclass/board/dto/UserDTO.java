package org.iclass.board.dto;

import lombok.*;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.UserEntity;

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
    private String userRole;
    private Date created_at;
    private String isActive;



    /* JPA 구간 */
    public static UserDTO of(UserEntity entity) {
        return UserDTO.builder()
                .user_id(entity.getUser_id())
                .username(entity.getUsername())
                .email(entity.getEmail())
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

