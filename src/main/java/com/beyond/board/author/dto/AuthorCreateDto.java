package com.beyond.board.author.dto;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class AuthorCreateDto {
    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    private String email;
    @NotEmpty(message = "비번은 필수 입력 항목입니다.")
    @Size(min = 8, message = "패스워드가 너무 짧습니다.")
    private String password;

    public Author authorToEntity(String encodedPassword) {
        return Author.builder()
                .name(this.name)
                .password(encodedPassword)
                .email(this.email)
                .role(Role.USER)
                .build();
    }
}
