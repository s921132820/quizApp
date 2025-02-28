package com.my.quizApp.controller;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private MemberService memberService;

    // 로그인 화면
    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("member", new MemberDto());
        return "login"; // login.html을 반환
    }

    @GetMapping("home")
    public String home(HttpSession session) {
        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
        if (loginUser !=null) {
            if("root".equals(loginUser.getId())) {
                return "redirect:/admin";
            } else {
                return "redirect:/member";
            }
        }
        return "redirect:/";
    }

    // 로그인 성공 후
    @PostMapping("/home")
    @ResponseBody
    public Map<String, Object> login(@RequestBody MemberDto member, HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        MemberDto findMember = memberService.findByIdAndPassword(member.getId(), member.getPassword());
        if (findMember != null) {
            session.setAttribute("loginUser", findMember);

            // 로그인 성공 시 리다이렉트할 URL 전달
            if ("root".equals(findMember.getId())) {
                response.put("status", "success");
                response.put("redirectUrl", "/admin");
            } else {
                response.put("status", "success");
                response.put("redirectUrl", "/member");
            }
        } else {
            response.put("status", "fail");
            response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return response;
    }

    @GetMapping("/admin")
    public String adminHome(
            HttpSession session,
            Model model
    ) {
        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");

        if (loginUser != null) {
            model.addAttribute("member", loginUser);
            return "admin/index";
        }
        return "redirect:/";
    }

    @GetMapping("/member")
    public String memberHome(
            HttpSession session,
            Model model
    ) {
        MemberDto loginUser = (MemberDto) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("member", loginUser);
            Long memberNo = loginUser.getNo();
            // answer_true와 answer_false 초기화
            memberService.resetAnswerCounts(memberNo);
            return "member/index";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return "redirect:/";
    }
}