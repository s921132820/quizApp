package com.my.quizApp.controller;

import com.my.quizApp.dto.QuizDto;
import com.my.quizApp.service.MemberService;
import com.my.quizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    QuizService quizService;

    @Autowired
    MemberService memberService;

    @GetMapping("/home")
    public String adminIndex(Model model) {
        List<QuizDto> quizList = quizService.getAllQuiz();
        model.addAttribute("quizList", quizList);
        return "admin/index";
    }

    // 퀴즈 등록 페이지
    @GetMapping("/insertQuiz")
    public String showQuizForm(Model model) {
        model.addAttribute("quiz", new QuizDto());
        return "admin/insertQuiz"; // 퀴즈 등록 폼
    }

    // 퀴즈 등록 처리
    @PostMapping("/quizSave")
    public String registerQuiz(@ModelAttribute QuizDto quizDto) {
        quizService.saveQuiz(quizDto); // 퀴즈 저장
        return "redirect:/admin/listQuiz"; // 퀴즈 목록 페이지로 리다이렉트
    }

    // 퀴즈 목록
    @GetMapping("/listQuiz")
    public String listQuiz(Model model) {
        model.addAttribute("quiz", quizService.getAllQuiz());
        return "admin/listQuiz"; // 퀴즈 목록 페이지
    }

    // 퀴즈 삭제
    @GetMapping("/delete/{no}")
    public String deleteQuiz(
            @PathVariable("no") Long no,
            RedirectAttributes redirectAttributes
            ) {
        redirectAttributes.addFlashAttribute("msg", "선택한 퀴즈가 삭제되었습니다");
        quizService.deleteQuiz(no); // 퀴즈 삭제
        return "redirect:/admin/listQuiz"; // 삭제 후 목록으로 리다이렉트
    }

    // 퀴즈 수정 페이지
    @GetMapping("/update/{no}")
    public String updateQuiz(
            @PathVariable("no") Long no, Model model
    ) {
        QuizDto quizDto = quizService.findById(no);
        model.addAttribute("quiz", quizDto);
        return "/admin/updateQuiz"; // 퀴즈 수정 폼
    }

        // 퀴즈 수정 처리
    @PostMapping("updateQuiz")
    public String updateQuiz(
            @ModelAttribute("quiz") QuizDto quizDto,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("msg", "선택한 퀴즈가 수정되었습니다");
        quizService.updateQuiz(quizDto); // 퀴즈 수정
        return "redirect:/admin/listQuiz"; // 수정 후 목록으로 리다이렉트
    }

    // 회원 목록 보기
    @GetMapping("/memberList")
    public String memberList(Model model) {
        model.addAttribute("memberList", memberService.findAllMember());
        return "admin/listMember";
    }

    // 회원 삭제하기
    @GetMapping("/deleteMember/{no}")
    public String deleteMember(
            @PathVariable("no") Long no,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("msg", "회원이 삭제되었습니다.");
        memberService.deleteMember(no);
        return "redirect:/admin/memberList";
    }
}
