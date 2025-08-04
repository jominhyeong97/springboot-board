package com.beyond.board.post.dto;

import com.beyond.board.author.domain.Author;
import com.beyond.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
    @Builder.Default
    private String appointment = "N";
    private String appointmentTime;
    private String category;


    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .category(category)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .author(author)
                .build();
    }
}
