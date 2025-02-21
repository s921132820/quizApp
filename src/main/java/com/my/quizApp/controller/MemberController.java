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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
//@RestController
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

    // 회원가입 시 멤버 아이디 중복체크
    @GetMapping("/checkId")
    @ResponseBody
    public Map<String, Boolean> checkId(@RequestParam String id) {
        log.info("####### 아이디 중복 확인 요청: " + id);
        boolean exists = memberService.isIdExists(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }
}
