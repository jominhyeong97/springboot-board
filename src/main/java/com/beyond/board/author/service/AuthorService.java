package com.beyond.board.author.service;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.dto.AuthorCreateDto;
import com.beyond.board.author.dto.AuthorDetailDto;
import com.beyond.board.author.dto.AuthorListDto;

import com.beyond.board.author.dto.AuthorLoginDto;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthorService {


private final AuthorRepository authorRepository;
private final PasswordEncoder passwordEncoder;


    public void save(AuthorCreateDto authorCreateDto) {
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);

        this.authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {

        return this.authorRepository.findAll().stream().
        map(a->a.listFromEntity()).collect(Collectors.toList());
    }

    public AuthorDetailDto findById(Long id) throws NoSuchElementException { //Optional객체에서 꺼내는것도 service의 역할
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 id 없습니다."));

        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);

        return dto;
    }

    public AuthorDetailDto findMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Author author = this.authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("해당 email 없습니다."));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);

        return dto;
    }

    public void delete(Long id) {

        Author author = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException());
        this.authorRepository.delete(author);
    }

    public Author login(AuthorLoginDto authorLoginDto) throws IllegalArgumentException {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(authorLoginDto.getEmail());
        boolean check = true;

        if(!optionalAuthor.isPresent()) {
            check = false;
        } else {
            if(!passwordEncoder.matches(authorLoginDto.getPassword(),optionalAuthor.get().getPassword())) {
                check = false;
            }
        }
        if(!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }

        return optionalAuthor.get();

    }



}
