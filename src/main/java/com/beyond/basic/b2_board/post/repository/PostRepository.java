package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    //    List<Post> findByAuthorIdAndTitleOrderByDesc(Long author_id, String title)
    //    select * from post where author_id? and title =? orderby createdTime desc;

//    List<Post> findByAuthorId(Long authorId);
// post의 변수명은 author(객체의필드명)지만 author_id(실질적 db의 컬럼명)으로 찾는것도 가능하다.

    List<Post> findByAuthor(Author author);

//    jpql을 사용한 일반 inner join
//    jpql는 기본적으로 lazy로딩 지향하므로 , inner join으로 필터링은 하되 post객체만 조회한다. ->n+1문제 여전히 발생
//    raw쿼리 : select p.* from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

//    jpql을 사용한 일반 fetch inner join
//    join시 post뿐만아니라 author 객체까지 한꺼번에 조립하여 조회 -> n+1문제 해결
//    raw쿼리 : select * from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();



//    paging처리(data.domain.Pageable를 임포트) & delyn 적용
//    page객체 안에 List<Post>, 전체페이지 수 등 정보 포함.
//    Pageable 객체 안에는 페이지 size, 페이지번호, 정렬기준 등이 포함.
    Page<Post> findAllByDelYnAndAppointment(Pageable pageable, String delYn, String appointment);


//    paging처리 + 검색(specification)을 위한 find 메서드
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    List<Post> findByAppointment(String appointment);

}
