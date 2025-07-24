package com.beyond.basic.b1_hello.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

//@Getter //클래스 내에 모든 변수를 대상으로 getter 생성
@Data //getter, setter, toString 메서드까지 모두 만들어주는 어노테이션
@AllArgsConstructor //모든 매개변수가 있는 생성사를 생성하는 어노테이션
@NoArgsConstructor

//기본생성자+getter로 parsing이 이뤄지므로 보통 필수적 요소

public class Hello {
    private String name;
    private String email;
//    MultipartFile photo;


}
