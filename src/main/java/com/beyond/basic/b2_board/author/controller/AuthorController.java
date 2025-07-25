package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.Common.JwtTokenProvider;
import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;
//     회원 가입
    @PostMapping("/create")
//    dto에 있는 validation어노테이션과 controller @vaild 한 쌍

    /* 아래 코드 포스트맨 테스트 데이터 예시
    1.multipart-formdata 선택
    2.authorCreateDto를 text로 선택하고 {"name":"유저","email":"test4@daum.net","password":"12341234"}
    로 세팅하면서 content-type을 application/json 설정
    3.profileImage는 file로 세팅하면서 content-type을 multipart/form-data 설정
     */

    public ResponseEntity<String> save(@RequestPart(name = "authorCreateDto") @Valid AuthorCreateDto authorCreateDto,
                                       @RequestPart(name = "profileImage", required = false)MultipartFile profileImage) {
        System.out.println(profileImage.getOriginalFilename());
//        try {
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//
////      생성자 매개면수 body부분의 객체와 header부에 상태코드
//            ResponseEntity<String> response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//            return response;
//        }
        this.authorService.save(authorCreateDto, profileImage);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }
    
//    회원목록 조회 : /author/list
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll() {
        return this.authorService.findAll();
    }
    
//    회원상세조회 : id로 조회 /author/detail/1
//    서버에서 별도의 try catch를 하지 않으면 에러 발생시 500에러 + 스프링의 포맷으로 에러를 리턴.
//    권한이 없응ㄹ 경우 filterchain에서 에러발생
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ADMIN')") //admin 권한이 있는지 authentication 객체에서 쉽게확인
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<?> findById(@PathVariable Long id) { /// ???
//        try {
////            ResponseEntity<AuthorDetailDto> response = new ResponseEntity<>(this.authorService.findById(id),HttpStatus.OK);
////            return response;
//            return new ResponseEntity<>(new CommonDto(this.authorService.findById(id),"Data has found",HttpStatus.OK.value()),HttpStatus.OK);
//        }
//        catch (NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(),e.getMessage()),HttpStatus.NOT_FOUND);
//        }
        return new ResponseEntity<>(new CommonDto(this.authorService.findById(id),"Data has found",HttpStatus.OK.value()),HttpStatus.OK);
    }

    @GetMapping("/myinfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findMyInfo() {
        authorService.findMyInfo();
        return new ResponseEntity<>(new CommonDto(this.authorService.findMyInfo(),"Data has found",HttpStatus.OK.value()),HttpStatus.OK);
    }

    
////    비밀번호 수정 : email, password > json
////    get:조회,post:등록,patch:부분수정, put:대체, delete
//    @PatchMapping("/updatepw")
//    public String updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        this.authorService.updatePassword(authorUpdatePwDto.getEmail(), authorUpdatePwDto.getPassword());
//        return "ok";
//    }

//    회원 탈퇴(삭제) : /author/1
    @DeleteMapping("/delete/{id}")
    public String delete (@PathVariable Long id) {
        this.authorService.delete(id);
        return "ok";
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> login(@Valid @RequestBody AuthorLoginDto authorLoginDto) {
        Author author = authorService.login(authorLoginDto);
//        토큰생성 및 return
        String token = jwtTokenProvider.createAtToken(author);
        return new ResponseEntity<>(new CommonDto(token,"token is created",HttpStatus.OK.value()),HttpStatus.OK);
    }

}
