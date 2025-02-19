package com.my.quizApp.service;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.entity.Member;
import com.my.quizApp.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return (member != null) ? new MemberDto(member.getId(), member.getPassword()) : null;
    }
}
