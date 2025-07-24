package com.beyond.basic.b2_board.author.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString


public class AuthorUpdatePwDto {
    private String email;
    private String password;

}
