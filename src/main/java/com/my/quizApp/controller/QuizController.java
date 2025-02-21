package com.my.quizApp.controller;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.service.MemberService;
import com.my.quizApp.service.QuizService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    public String getRandomQuizzes
    (
            @RequestParam(defaultValue = "3") int count,
            HttpSession session, Model model
    ) {
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
        // 세션에 저장 (현재 인덱스도 추가)
        session.setAttribute("quizzes", quizzes);
        session.setAttribute("currentIndex", 0);

        return "member/quizGame";  // member/quizGame.html 템플릿 렌더링
    }

    @PostMapping("/quizGame/next")
    public ResponseEntity<Map<String, Object>> nextQuiz(HttpSession session) {
        List<QuizDto> quizzes = (List<QuizDto>) session.getAttribute("quizzes");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (quizzes == null || currentIndex == null) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, Object> response = new HashMap<>();

        if (currentIndex >= quizzes.size()) {
            response.put("submit", true);
            return ResponseEntity.ok(response);
        }

        QuizDto currentQuiz = quizzes.get(currentIndex);
        session.setAttribute("currentIndex", currentIndex + 1);

        response.put("submit", false);
        response.put("quiz", currentQuiz);
        return ResponseEntity.ok(response);
    }

    // 답안 제출 및 결과 처리
    @PostMapping("/member/submitAnswers")
    public String submitAnswers
    (
            @RequestParam Map<String, String> answers,
            HttpSession session, Model model
    ){
        int correctCount = 0;
        int wrongCount = 0;

        if (answers == null || answers.isEmpty()) {
            System.err.println("❌ 클라이언트에서 받은 답안이 없습니다!");
            return "redirect:/";
        }



        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            Long memberNo = loginUser.getNo();  // 로그인한 회원의 ID 가져오기

            for (String key : answers.keySet()) {
                try {
                    if (!key.startsWith("answer_")) { // "answer_"로 시작하지 않는 key는 무시
                        continue;
                    }

                    System.out.println("처리 중인 답안: " + key + " -> " + answers.get(key));

                    Long quizNo = Long.valueOf(key.split("_")[1]); // 안전한 파싱
                    boolean userAnswer = Boolean.parseBoolean(answers.get(key));

                    QuizDto quiz = quizService.getQuizByNo(quizNo);
                    if (quiz != null) {
                        if (quiz.isAnswer() == userAnswer) {
                            correctCount++;
                        } else {
                            wrongCount++;
                        }
                        quizService.updateMemberAnswerCounts(memberNo, quiz.isAnswer() == userAnswer);
                    }
                } catch (Exception e) {
                    System.err.println("❌ 퀴즈 처리 중 오류 발생: " + key);
                    e.printStackTrace();
                }
            }
            // 로그 출력
            System.out.println("맞은 개수: " + correctCount);
            System.out.println("틀린 개수: " + wrongCount);

            session.setAttribute("correctCount", correctCount);
            session.setAttribute("wrongCount", wrongCount);
            return "member/result";
        }

        return "redirect:/";
    }

    @GetMapping("/result")
    public String showResultPage(
            Model model,
            HttpSession session
    ) {
        // 세션에서 데이터 가져오기
        Integer correctCount = (Integer) session.getAttribute("correctCount");
        Integer wrongCount = (Integer) session.getAttribute("wrongCount");

        System.out.println("GET 요청 -> 맞은 개수: " + correctCount + ", 틀린 개수: " + wrongCount);

        // model에 값 추가
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("wrongCount", wrongCount);

        return "member/result"; // member/result.html을 보여줌
    }
}
