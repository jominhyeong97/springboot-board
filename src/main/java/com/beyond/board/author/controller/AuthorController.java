package com.beyond.board.author.controller;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.dto.*;
import com.beyond.board.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;


    @GetMapping("/create")
    public String createScreen() {

        return "author/author_register";
    }

    @PostMapping("/create")
    public String save(@Valid AuthorCreateDto authorCreateDto) {
        this.authorService.save(authorCreateDto);
        return "redirect:/";
    }





    @GetMapping("/list")
    public String findAll(Model model) {
        List<AuthorListDto> authorListDtoList = authorService.findAll();
        model.addAttribute("authorList", authorListDtoList);
        return "author/author_list";
    }





    @GetMapping("/detail/{id}")
    public String findById(@PathVariable Long id, Model model) {
        AuthorDetailDto authorDetailDto = this.authorService.findById(id);
        model.addAttribute("author",authorDetailDto);
        return "author/author_detail";
    }



////    비밀번호 수정 : email, password > json
////    get:조회,post:등록,patch:부분수정, put:대체, delete
//    @PatchMapping("/updatepw")
//    public void updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        this.authorService.updatePassword(authorUpdatePwDto.getEmail(), authorUpdatePwDto.getPassword());
//    }

//    회원 탈퇴(삭제) : /author/1
    @DeleteMapping("/delete/{id}")
    public String delete (@PathVariable Long id) {
        this.authorService.delete(id);
        return "ok";
    }



}
