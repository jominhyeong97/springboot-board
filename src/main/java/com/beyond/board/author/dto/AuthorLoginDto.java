package com.beyond.board.author.dto;

import com.beyond.board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data


public class AuthorLoginDto {
    @Column(length = 20)
    private String email;
    @Column(length = 20)
    private String password;


    public Author toEntity() {
        return Author.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }

}
