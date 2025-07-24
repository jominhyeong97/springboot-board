package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Repository
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;


//    회원 가입
    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

//    전체목록조회
    public List<Author> findAll() {
        return this.authorList;
    }

//    상세조회
    public Optional<Author> findById(Long id) {

        for(Author a : authorList) {
            if(a.getId().equals(id)) {
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }


    public Optional<Author> findByEmail(String email) {
        Author author = null;
        for(Author a : this.authorList) {
            if(a.getEmail().equals(email)) {
                author = a;
            }
        }
        return Optional.ofNullable(author);
    }



    public void delete (Long id) {
//        id값으로 인덱스값을 찾아 삭제

        for(int i=0; i<this.authorList.size(); i++) {
            if(this.authorList.get(i).getId().equals(id)) {
                authorList.remove(i);
                break;
            }
        }

    }

}
