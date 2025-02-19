package com.my.quizApp.repository;

import com.my.quizApp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // id와 password로 회원 조회
    @Query("SELECT m FROM Member m WHERE m.id = :id AND m.password = :password")
    Member findByIdAndPassword(String id, String password);

}
