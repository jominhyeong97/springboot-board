package com.beyond.board.post.service;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.post.domain.Post;
import com.beyond.board.post.dto.PostCreateDto;
import com.beyond.board.post.dto.PostDetailDto;
import com.beyond.board.post.dto.PostListDto;
import com.beyond.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;



    public void save(PostCreateDto dto) throws IllegalArgumentException{
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName(); //이름이 아니라, subject를 의미 : email
        LocalDateTime appointmentTime = null;
        Author author = authorRepository.findByEmail("admin@naver.com").orElseThrow(()->new EntityNotFoundException("없는 ID입니다."));
        if(dto.getAppointment().equals("Y")) {
            if(dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비어져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(),dateTimeFormatter);
        }

        postRepository.save(dto.toEntity(author,appointmentTime));

    }

    public Page<PostListDto> findAll(Pageable pageable) {

        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(PostListDto::fromEntity);


    }

    public PostDetailDto findById(Long id) {

        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 ID입니다."));

        return PostDetailDto.fromEntity(post);

    }

}
