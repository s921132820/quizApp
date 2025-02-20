package com.my.quizApp.service;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.entity.Member;
import com.my.quizApp.entity.Quiz;
import com.my.quizApp.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public void saveMember(@Valid MemberDto dto) {
        // DTO -> Entity
        Member member = MemberDto.fromDto(dto);

        // 저장요청
        memberRepository.save(member);
    }

    public MemberDto findByIdAndPassword(String id, String password) {
        // id와 password로 회원 조회
        Member member = memberRepository.findByIdAndPassword(id, password);
        if (member != null) {
            // member 엔티티에서 값을 가져와 MemberDto 생성
            return new MemberDto(
                    member.getNo(),
                    member.getId(),
                    member.getPassword(),
                    member.getAnswerTrue(),  // answerTrue 값 추가
                    member.getAnswerFalse()  // answerFalse 값 추가
            );
        } else {
            // 해당 회원이 없으면 null 반환
            return null;
        }
    }

    // 사용자의 정답 개수만 초기화
    public void resetAnswerCounts(Long memberNo) {
        Optional<Member> memberOpt = memberRepository.findById(memberNo);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setAnswerTrue(0);
            member.setAnswerFalse(0);
            memberRepository.save(member);  // 엔티티 저장
            memberRepository.flush();  // DB에 반영
        }
    }

    public Object findAllMember() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(MemberDto::fromEntity).toList();
    }

    public void deleteMember(Long no) {
        memberRepository.deleteById(no);
    }
}