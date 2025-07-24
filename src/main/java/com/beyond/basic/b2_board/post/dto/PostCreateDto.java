package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PostCreateDto {
    @NotEmpty //숫자는 NotNull
    private String title;
    private String contents;
    @Builder.Default
    private String appointment = "N";
//    시간정보는 직접 LocalDateTime으로 형변환 하는 경우가 많음.
    private String appointmentTime;
    private String category;


    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)
                .category(category)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .author(author)
                .build();
    }
}
