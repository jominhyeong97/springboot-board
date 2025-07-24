package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public class AuthorJpaRepository {

    @Autowired
    private EntityManager entityManager;

    public void save(Author author) {
//        persist : 순수 JPA에서 data를 insert 하는 메서드
        entityManager.persist(author);
    }

    public List<Author> findAll() {
//        순수 JPA에서는 제한된 메서드 제공으로 JPQL을 사용하여 직접 쿼리작성하는 경우가 많음.
//        JPQL : JPQL문법은 문자열형식의 raw 쿼리가 아닌 객체지향 쿼리문
//        jpql 작성규칙 : db 테이블명/컬럼명이 아니라, 엔티티명/필드명을 기준으로 사용하고, 별칭(alias)를 활용
        List<Author> authorList = entityManager.createQuery("select a from Author a",Author.class).getResultList();
        return authorList;
    }

    public Optional<Author> findById(Long id) {
//        find는 pk로 조회하는 경우에 select문 자동완성
        Author author = entityManager.find(Author.class,id);
        return Optional.ofNullable(author);
    }

    public Optional<Author> findByEmail(String email) {
        Author author = null;

        try {
            author = entityManager.createQuery("select a from Author a where a.email = :email", Author.class)
                    .setParameter("email",email).getSingleResult();
        } catch (Exception e) {

        }
        return Optional.ofNullable(author);
    }

    public void delete (Long id) {
//        remove(객체) : 객체삭제
        Author author = entityManager.find(Author.class,id);
        if(author != null) {
            entityManager.remove(author);
        }
    }

}
