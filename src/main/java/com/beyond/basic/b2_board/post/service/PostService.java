package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service

public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto) throws IllegalArgumentException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); //이름이 아니라, subject를 의미 : email
        LocalDateTime appointmentTime = null;
        Author author = authorRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("없는 ID입니다."));
        if(dto.getAppointment().equals("Y")) {
            if(dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비어져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(),dateTimeFormatter);
        }

        postRepository.save(dto.toEntity(author,appointmentTime));

    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto postSearchDto) {
//         List<PostListDto> postList = new ArrayList<>();
//         for(Post a : postRepository.findAll()) {
//            postList.add(PostListDto.fromEntity(a));
//         }
//         return postList;

//        postList를 조회할 때 참조관계에 있는 author까지 조회하게 되므로, N(author쿼리)+1(post쿼리)문제 발생
//        jpa는 기본방샹성이 fetch lazy이므로 참조하는 시점에 쿼리를 내보내게 되어 join을 하지 않고 n+1문제 발생

//        List<Post> postList = postRepository.findAllJoin(); //일반 inner join
//        List<Post> postList = postRepository.findAllFetchJoin(); //fetch inner join

//        검색을 위해 Spring에서 Specification 객체 사용
//        Specification객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로 쿼리를 쉽게 생성.
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                Root : 엔티티의 속성에 접근하기 위한 객체(조건), CriteriaQuery : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("delYn"),"N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"),"N"));

                if(postSearchDto.getCategory()!=null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"),postSearchDto.getCategory()));
                }
                if(postSearchDto.getTitle()!=null) {
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%"+postSearchDto.getTitle()+"%"));
                }

                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for(int i=0; i< predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }
//                위의 검색 조건들을 하나(한줄)의 Predicate 객체로 만들어서 return
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }


        };

        Page<Post> postList = postRepository.findAll(specification ,pageable);
        return postList.map(PostListDto::fromEntity);


    }

    public PostDetailDto findById(Long id) {
//        엔티티간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getId()).orElseThrow(()-> new EntityNotFoundException("없는회원입니다."));
//        return PostDetailDto.fromEntity(post, author);


//        페이지처리 findALL호출
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 ID입니다."));

        return PostDetailDto.fromEntity(post);

//        엔티티간의 관계성 설정을 통해 Author객체를 쉽게 조회하는 경우


    }

}
