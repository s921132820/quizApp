package com.my.quizApp.service;

import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.entity.Member;
import com.my.quizApp.entity.Quiz;
import com.my.quizApp.exception.MemberNotFoundException;
import com.my.quizApp.repository.MemberRepository;
import com.my.quizApp.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    MemberRepository memberRepository;

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

    public List<QuizDto> getRandomQuiz(int count) {
        List<Quiz> quizList = quizRepository.findAll();  // 모든 퀴즈 가져오기
        Collections.shuffle(quizList);  // 리스트 섞기

        // 가져올 개수 결정 (최대 count개, 하지만 리스트 크기보다 크면 전체 반환)
        int limit = Math.min(count, quizList.size());

        // 랜덤으로 선택된 퀴즈를 DTO로 변환하여 반환
        return quizList.subList(0, limit)
                .stream()
                .map(QuizDto::fromQuizEntity)
                .toList();
    }

    // 퀴즈 번호로 퀴즈 하나를 반환하는 메서드
    public QuizDto getQuizByNo(Long no) {
        // 퀴즈 번호로 해당 퀴즈를 DB에서 찾기
        Quiz quiz = quizRepository.findById(no).orElse(null);  // 없으면 null 반환

        // 퀴즈가 존재하면 QuizDto로 변환 후 반환
        if (quiz != null) {
            return QuizDto.fromQuizEntity(quiz);  // Quiz 엔티티를 QuizDto로 변환
        }
        return null;  // 퀴즈가 없으면 null 반환
    }

    // 정답 처리 및 맞은/틀린 개수 업데이트
    @Transactional
    public void updateMemberAnswerCounts(Long no, boolean isCorrect) {
        // Member를 아이디로 찾기
        Optional<Member> optionalMember = memberRepository.findById(no);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 정답 또는 오답에 따라 카운트 증가
            if (isCorrect) {
                member.setAnswerTrue(member.getAnswerTrue() + 1);  // 맞은 개수 증가
                logger.info("Correct answer. Updated answerTrue: {}", member.getAnswerTrue());
            } else {
                member.setAnswerFalse(member.getAnswerFalse() + 1);  // 틀린 개수 증가
                logger.info("Wrong answer. Updated answerFalse: {}", member.getAnswerFalse());
            }

            // 변경된 데이터를 DB에 저장
            System.out.println("Saving member: " + member);

            memberRepository.save(member);
            memberRepository.flush(); // 엔티티 변경 사항을 즉시 DB에 반영
        } else {
            // Member가 존재하지 않을 때 처리
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
        }
    }


}
