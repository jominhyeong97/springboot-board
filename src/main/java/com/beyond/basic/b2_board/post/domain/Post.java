package com.beyond.basic.b2_board.post.domain;


import com.beyond.basic.b2_board.Common.BaseTimeEntity;
import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.*;
import lombok.*;

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



//    fk설정시 ManyToOne필수
//    ManyToOne에서는 default fetch.EAGER(즉시로딩) : author객체를 사용하지 않아도 author테이블로 쿼리발생
//    그래서 일반적으로 fetch.LAZY(지연로딩)설정 : author 객체를 사용하지 않는 한 author테이블로 쿼리발생 x
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") //fk관계성 설정
    private Author author;

    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }

}
