package org.iclass.board.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private int userId;
    private String username;
    private String password;
    private String email;
//    private String userRole;
    private Date createdAt;
    private String isActive;
}

