package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer postCount;
    private LocalDateTime createdTime;

//    1개의 entity로만 dto가 조립되는것이 아니기에 dto계층에서 fromEntity를 설계

//    public static AuthorDetailDto fromEntity(Author author,Integer postCount) {

        public static AuthorDetailDto fromEntity(Author author) {

        return AuthorDetailDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
                .postCount(author.getPostList().size())
                .createdTime(author.getCreatedTime())
                .build();
    }
}
