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
//    private Long no;
    private String id;
    private String password;

    // Entity -> DTO
    public static MemberDto fromEntity(Member entity) {
        return new MemberDto(
//                entity.getNo(),
                entity.getId(),
                entity.getPassword()

        );
    }

    // DTO -> Entity
    public static Member fromDto(@Valid MemberDto dto) {
        Member member = new Member();
//        member.setNo(dto.getNo());
        member.setId(dto.getId());
        member.setPassword(dto.getPassword());
        return member;
    }
}
