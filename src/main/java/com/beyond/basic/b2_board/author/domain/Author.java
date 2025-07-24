package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.Common.BaseTimeEntity;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString

//JPA를 사용할 경우 Entity에 반드시 붙여야하는 어노테이션
//JPA의 Entity Manager에게 객체를 위임하기 위한 어노테이션
//Entity Manager는 영속성 컨텍스트(엔티티의 현재 상황)를 통해 db데이터 관리
@Entity
//BUilder 어노테이션을 통해 유연하게 객체생성 가능
@Builder

public class Author extends BaseTimeEntity {
    @Id //pk로 설정
//    identity : auto_increment, auto : id생성전략을 jpa에게 자동설정하도록 위임
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    컬럼에 별다른 설정이 없을경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
//    @Column(name = "pw") 되도록이면 컬럼명과 필드명을 일칳시키는 것이 개발의 혼선을 줄일 수 있음.
    private String password;
    @Builder.Default //빌더 패턴에서 변수 초기화(디폴트값)시 Builder.Default어노테이션 필수
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String profileImage;

    //    OneToMany는 선택사항이다. 또한 default가 lazy.
    //mappedBy에는 manyToOne쪽의 변수명을 문자열로 저장. 초기화 필수!
    // fk관리를 반대편쪽(post)에서 한다는 의미 -> 연관관계의 주인설정
//    cascade : 부모객체의 변화에 따라 자식객체가 같이 변하는 옵션 1)persists : 저장 2)remove : 삭제
//   자식의 자식까지 모두 삭제할 경우 orphanRemoval = ture 추가
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default //중요!!!!!!!!
    List<Post> postList = new ArrayList<>();

    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;


    public void updatePw(String password) {
        this.password = password;
    }

//    public AuthorDetailDto detailFromEntity() {
//        return  new AuthorDetailDto(this.id,this.name,this.email);
//    }

    public AuthorListDto listFromEntity() {
        return new AuthorListDto(this.id,this.name,this.email);
    }

    public void updateImageUrl(String url) {
        this.profileImage = url;
    }
}
