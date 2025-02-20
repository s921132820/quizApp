package com.my.quizApp.repository;

import com.my.quizApp.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // id와 password로 회원 조회
    @Query("SELECT m FROM Member m WHERE m.id = :id AND m.password = :password")
    Member findByIdAndPassword(String id, String password);

    // 사용자의 정답개수 초기화
    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.answerTrue = 0, m.answerFalse = 0 WHERE m.id = :memberNo")
    void resetAnswerCounts(@Param("memberNo") Long memberNo);


}
