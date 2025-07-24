//package com.beyond.basic.b2_board.repository;
//
//import com.beyond.basic.b2_board.domain.Author;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//
//@Repository
//
//public class AuthorJdbcRepository {
//
//
////    data source는 DB와 JDBC에서 사용하는 DB연결 드라이버 객체
////    application.yml에 설정한 DB정보를 사용하여 datasource 객체 싱글톤 생성
//    @Autowired
//    private DataSource dataSource;
//
//
//    //    jdbc의 단점
////    1.raw쿼리에서 오타가 나도 디버깅 어려움
////    2.data 추가시, 매개변수와 column의 mapping을 수작업
////    3.data 조회시, 객체 조립을 직접해야함.
//    public void save(Author author) {
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "insert into author(email, name, password) values(?,?,?)";
////            PreparedStatement 객체로 만들어서 실행가능한 상태로 만드는 것.
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1,author.getEmail());
//            ps.setString(2,author.getName());
//            ps.setString(3,author.getPassword());
//            ps.executeUpdate(); //추가, 수정의 경우는 excuteUpdate, 조회는 excuteQuery
//        } catch (SQLException e) {
////            unchecked 예외는 spring 트랜잭션 상황에서 rollback의 기준이 된다.
//            throw new RuntimeException(e);
//        }
//    }
//
//    //    전체목록조회
//    public List<Author> findAll() {
//        List<Author> author = new ArrayList<>();
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select * from author";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                author.add(new Author(id, name, email, password));
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return author;
//    }
//
//    //    상세조회
//    public Optional<Author> findById(Long inputId) {
//        Author author = null;
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select *from author where id = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setLong(1,inputId);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()) {
//                rs.next();
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                author = new Author(id,name,email,password);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return Optional.ofNullable(author);
//    }
//
//
//    public Optional<Author> findByEmail(String inputEmail) {
//        Author author = null;
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select *from author where email = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1,inputEmail);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()) {
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                author = new Author(id,name,email,password);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return Optional.ofNullable(author);
//    }
//
//
//
//    public void delete (Long id) {
////        id값으로 인덱스값을 찾아 삭제
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "delete from author where id=?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setLong(1,id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void updatePassword() {
//
//    }
//
//}
