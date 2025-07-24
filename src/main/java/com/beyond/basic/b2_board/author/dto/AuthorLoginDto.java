package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
