package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.Common.AwsS3Config;
import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;

import com.beyond.basic.b2_board.author.dto.AuthorLoginDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


//스프링에서 메서드 단위로 트랜잭션처리(commit)를 하고 만약 예외(unchecked) 발생시 자동 롤백처리 지원.
@Transactional //(DB의)자동커밋과 같은 역할
@Service //Component로 대체 가능(트랜잭션처리가 없는 경우)

public class AuthorService {

//    의존성 주입(DI) 방법1. Autowired 어노테이션 사용 -> 필드주입

//    @Autowired
//    private AuthorRepository authorRepository;

//    의존성 주입(DI) 방법2. 생성자 주입방식(가장 많이 쓰임)
//    장점 1. final을 통해 상수로 사용가능(안정성향상) 2. 다형성구현가능 3. 순환참조방지(컴파일 타임에 체크) ????

//    private final AuthorRepositoryInterface authorMemoryRepository;
////    객체로 만들어지는 시점에 Spring 에서 authorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없을 때에는 auto wired 생략가능
//    public AuthorService(AuthorMemoryRepository authorMemoryRepository) {
//        this.authorMemoryRepository = authorMemoryRepository;
//    }


//    의존성 주입(DI) 방법3. RequiredArgs 어노테이션 사용 -> 반드시 초기화되어야 하는 필드(final 등)을 대상으로 생성자를 자동생성
//    다형성 설계는 불가

private final AuthorRepository authorRepository;
private final PostRepository postRepository;
private final PasswordEncoder passwordEncoder;
private final S3Client s3Client;

@Value("${cloud.aws.s3.bucket}")
private String bucketName;

@Autowired
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository, PasswordEncoder passwordEncoder, AwsS3Config awsS3Config, S3Client s3Client) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
}


    public void save(AuthorCreateDto authorCreateDto, MultipartFile profileImage) {
//        이메일 중복검증, 비밀번호 길이 검증

//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encodedPassword);

//        cascading 테스트 : 회원이 생성될 때, 곧바로 "가입인사"글을 생성하는 상황
//        방법1. 직접 POST객체 생성 후 저장
//        방법2. cascade 옵션 활용
//        author.getPostList().add(post);
        this.authorRepository.save(author);

        if(profileImage != null) {

//        image명 설정
            String fileName = "user-"+ author.getId()+ "-profileImage-" + profileImage.getOriginalFilename();

//        저장 객체 구성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(profileImage.getContentType()) //image/jpeg, video/mp4 ...
                    .build();

//        이미지를 업로드(byte형태로)
            try {
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(profileImage.getBytes()));
            }
            catch (Exception e) {
//            checked -> unchecked로 바꿔 전체 rollback되도록 예외처리
                throw new IllegalArgumentException("이미지 업로드 실패");
            }

//        이미지를 추출
            String imgUrl = s3Client.utilities().getUrl(a->a.bucket(bucketName).key(fileName)).toExternalForm();
            author.updateImageUrl(imgUrl);
        }

    }

//    트랜잭션이 필요 없는 경우, 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {

//        List<AuthorListDto> authorListDto = new ArrayList<>();
//        for(Author a : this.authorMemoryRepository.findAll()) {
//            AuthorListDto authorDto = a.listFromEntity();
//            authorListDto.add(authorDto);
//        }

//        트랜잭션이 필요없는 경우 위 코드를 간결하게 줄임.

        return this.authorRepository.findAll().stream().
        map(a->a.listFromEntity()).collect(Collectors.toList());
    }

//    public void updatePassword(String email, String newPassword) {
//
//        AuthorUpdatePwDto authorUpdatePwDto = new AuthorUpdatePwDto();
//        for(Author a : this.authorRepository.getAuthorList()) {
//            if(a.getEmail().equals(email)) {
//                a.updatePw(newPassword);
//                return;
//            }
//        }
////        dirty체킹 : 객체를 수정한 후 별도의 update 쿼리 발생시키지 않아도 영ㅎ속성 컨텍스트에 의해 객체변경사항 자동 DB반영
//        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail().o
//
//
//    }

    public AuthorDetailDto findById(Long id) throws NoSuchElementException { //Optional객체에서 꺼내는것도 service의 역할
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 id 없습니다."));
//        List<Post> postList = postRepository.findByAuthorId(id); //이것도 가능

//          연관관계설정없이 직접 조회해서 count값 찾는 경우
//        List<Post> postList = postRepository.findByAuthor(author);
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author,postList.size());

//        oneToMany 연관관계 설정을 통해 count값 찾는 경우
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
//                    비밀번호 일치여부 검증 : matches 함수를 통해서 암호되지 않은 값을 다시 암호화하여 db의 password를 검증
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
