package com.beyond.board.post.domain;


import com.beyond.board.Common.BaseTimeEntity;
import com.beyond.board.author.domain.Author;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000, nullable = false)
    private String contents;
    @Builder.Default
    private String delYn = "N";
    @Builder.Default
    private String appointment = "N";
    private LocalDateTime appointmentTime;
    private String category;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") //fk관계성 설정
    private Author author;

    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }

}
