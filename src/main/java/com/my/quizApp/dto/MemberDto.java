package com.my.quizApp.dto;

import com.my.quizApp.entity.Member;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long no;
    private String id;
    private String password;
    private int answerTrue;
    private int answerFalse;

    // Entity -> DTO
    public static MemberDto fromEntity(Member entity) {
        return new MemberDto(
                entity.getNo(),
                entity.getId(),
                entity.getPassword(),
                entity.getAnswerTrue(),
                entity.getAnswerFalse()
        );
    }

    // DTO -> Entity
    public static Member fromDto(@Valid MemberDto dto) {
        Member member = new Member();
        member.setNo(dto.getNo());
        member.setId(dto.getId());
        member.setPassword(dto.getPassword());
        member.setAnswerTrue(dto.getAnswerTrue());
        member.setAnswerFalse(dto.getAnswerFalse());
        return member;
    }
}
