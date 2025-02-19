package com.my.quizApp.controller;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.service.MemberService;
import com.my.quizApp.service.QuizService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberController {
    // 생성자 주입
    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    QuizService quizService;

    @GetMapping("signup")
    public String signup(Model model) {
        log.info("####### signup 페이지 요청됨!");
        model.addAttribute("memberDto", new MemberDto());
        return "member/signup";
    }

    @PostMapping("insert")
    public String insertMember(
            @Valid MemberDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        // validation check 진행
        if(bindingResult.hasErrors()) {
            // 오류 있을 때
            log.info("####### validation Error!");
            return "signup";
        }

        // 받은 dto를 service에 넘겨주고 저장 요청
        memberService.saveMember(dto);
        redirectAttributes.addFlashAttribute("msg", "신규데이터가 입력되었습니다");
        return "redirect:/";
    }

    // 퀴즈 게임 관련
    @GetMapping("quizGame")
    public String quizGame(Model model) {
        QuizDto quizDto = quizService.getRandomQuiz();
        model.addAttribute("quiz", quizDto);
        return "member/quizGame";
    }


}
