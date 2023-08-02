package javaproject.project.Repository;

import javaproject.project.Entity.Board;
import javaproject.project.Entity.User_T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User_T, Integer> {
    Page<User_T> findAll(Pageable pageable);
    Optional<User_T> findByLoginId(String loginId);
    Optional<User_T> findByEmail(String email);
    List<User_T> findAllByEmail(String email);
    boolean existsByLoginId(String loginId);//아이디검사
}
