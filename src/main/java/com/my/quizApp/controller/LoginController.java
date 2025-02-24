package com.my.quizApp.controller;

import com.my.quizApp.dto.MemberDto;
import com.my.quizApp.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login(
            @ModelAttribute("member") MemberDto member,
            HttpSession session
    ) {
        MemberDto findMember = memberService.findByIdAndPassword(member.getId(), member.getPassword());
        if (findMember != null) {
            session.setAttribute("loginUser", findMember);
            // id가 root이면 admin.html로 이동
            if ("root".equals(findMember.getId())) {
                return "redirect:/admin";
            } else {
                return "redirect:/member"; // 일반 유저는 index.html
            }
        }
        return "redirect:/login?error=true"; // 로그인 실패 시 다시 로그인 페이지로
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