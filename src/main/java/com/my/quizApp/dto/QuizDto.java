package com.my.quizApp.dto;

import com.my.quizApp.entity.Quiz;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private Long no;
    private String content;
    private boolean answer; // 정답 여부

    //Entity -> DTO
    public static QuizDto fromQuizEntity(Quiz quizEntity) {
        return new QuizDto(
                quizEntity.getNo(),
                quizEntity.getContent(),
                quizEntity.isAnswer()
        );
    }

    //DTO -> Entity
    public static Quiz fromQuizDto(@Valid QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setNo(quizDto.getNo());
        quiz.setContent(quizDto.getContent());
        quiz.setAnswer(quizDto.isAnswer());
        return quiz;
    }
}