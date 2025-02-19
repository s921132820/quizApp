package com.my.quizApp.service;

import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.entity.Quiz;
import com.my.quizApp.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;

    public List<QuizDto> getAllQuiz() {
        List<Quiz> quizList = quizRepository.findAll();
        return quizList.stream().map(QuizDto::fromQuizEntity).toList();
    }

    public void saveQuiz(QuizDto quizDto) {
        Quiz quiz = QuizDto.fromQuizDto(quizDto);
        quizRepository.save(quiz);
    }

    public void deleteQuiz(Long no) {
        quizRepository.deleteById(no);
    }

    // 해당 퀴즈 찾기
    public QuizDto findById(Long no) {
        //id(no)로 검색하기
        Quiz quiz = quizRepository.findById(no).orElse(null);

        //dto로 변환
        if (!ObjectUtils.isEmpty(quiz)) {
            return QuizDto.fromQuizEntity(quiz);
        } else {
            return null;
        }
    }



    // 해당 퀴즈 업데이트
    public void updateQuiz(QuizDto quizDto) {
        // dto -> entity로 변환
        Quiz quiz = QuizDto.fromQuizDto(quizDto);

        //수정처리
        quizRepository.save(quiz);
    }

    public QuizDto getRandomQuiz() {
        List<Quiz> quizList = quizRepository.findAll();
        Collections.shuffle(quizList);
        Quiz randomQuiz = quizList.get(0);
        return QuizDto.fromQuizEntity(randomQuiz);
    }
}
