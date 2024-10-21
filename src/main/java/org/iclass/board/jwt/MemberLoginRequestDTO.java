package org.iclass.board.jwt;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequestDTO {
    private String userId;
    private String password;
}
