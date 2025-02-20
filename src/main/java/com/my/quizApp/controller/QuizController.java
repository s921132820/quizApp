package com.my.quizApp.controller;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.service.MemberService;
import com.my.quizApp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class QuizController {
    @Autowired
    QuizService quizService;

    @Autowired
    MemberService memberService;

    // 퀴즈 게임 관련
    @GetMapping("/quizGame")
    public String getRandomQuizzes(@RequestParam(defaultValue = "10") int count, HttpSession session, Model model) {
        // 로그인된 사용자의 ID 가져오기
        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");

        if (loginUser != null) {
            Long memberNo = loginUser.getNo();
            // 정답 개수를 초기화
            memberService.resetAnswerCounts(memberNo);
        } else {
            return "redirect:/";
        }

        // 퀴즈 가져오기
        List<QuizDto> quizzes = quizService.getRandomQuiz(count);
        model.addAttribute("quizzes", quizzes);  // 퀴즈 데이터를 모델에 추가

        return "member/quizGame";  // member/quizGame.html 템플릿 렌더링
    }


    // 답안 제출 및 결과 처리
    @PostMapping("/member/submitAnswers")
    public String submitAnswers(@RequestParam Map<String, String> answers, HttpSession session, Model model) {
        int correctCount = 0;
        int wrongCount = 0;


        // 로그인된 사용자의 ID 가져오기
        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            Long memberNo = loginUser.getNo();  // 로그인한 회원의 ID를 가져오기

            // 정답 비교
            for (String key : answers.keySet()) {
                // key는 "answer_퀴즈번호" 형태임 (예: "answer_1")
                Long quizNo = Long.valueOf(key.split("_")[1]);
                boolean userAnswer = Boolean.parseBoolean(answers.get(key));  // 사용자의 답 (O: true, X: false)

                // 해당 퀴즈를 가져와 정답 비교
                QuizDto quiz = quizService.getQuizByNo(quizNo);
                if (quiz != null) {
                    if (quiz.isAnswer() == userAnswer) {
                        correctCount++;
                    } else {
                        wrongCount++;
                    }
                    // 정답 및 오답에 따라 카운트 DB 업데이트
                    quizService.updateMemberAnswerCounts(memberNo, quiz.isAnswer() == userAnswer);
                }
            }

            // 맞은 개수와 틀린 개수를 모델에 추가
            model.addAttribute("correctCount", correctCount);
            model.addAttribute("wrongCount", wrongCount);
            return "member/result";  // 결과 페이지로 이동
        }

        // 로그인된 사용자가 없으면 오류 페이지로 이동
        return "redirect:/";
    }
}
