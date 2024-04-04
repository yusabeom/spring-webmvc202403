package com.spring.mvc.chap05.controller;

import com.spring.mvc.chap05.dto.request.LoginRequestDTO;
import com.spring.mvc.chap05.dto.request.SignUpRequestDTO;
import com.spring.mvc.chap05.service.LoginResult;
import com.spring.mvc.chap05.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 양식 화면 요청
    // 응답하고자 하는 화면의 경로가 url과 동일하다면 void로 처리할 수 있습니다. (선택사항)
    @GetMapping("/sign-up")
    public void signUp() {
        log.info("/members/sign-up: GET!!");
//        return "members/sign-up";
    }

    // 아이디, 이메일 중복체크 비동기 요청 처리
    @GetMapping("/check/{type}/{keyword}")
    @ResponseBody
    public ResponseEntity<?> check(@PathVariable String type,
                                   @PathVariable String keyword) {
        log.info("/members/check: GET!!");
        log.debug("type: {}, keyword: {}", type, keyword);

        boolean flag = memberService.checkDuplicateValue(type, keyword);

        return ResponseEntity.ok().body(flag);

    }

    @PostMapping("/sign-up")
    public String signUp(SignUpRequestDTO dto) {
        log.info("/members/sign-up: POST!!, dto: {}", dto);

        memberService.join(dto);
        return "redirect:/board/list";
    }

    // 로그인 양식 화면 요청 처리
    @GetMapping("/sign-in")
    public void signIn() {
        log.info("/members/sign-in: GET!!");
    }

    // 로그인 검증 요청
    @PostMapping("/sign-in")
    public String signIn(LoginRequestDTO dto, RedirectAttributes ra,
                         HttpServletResponse response,
                         HttpServletRequest request) {
        log.info("/members/sign-in: POST!!, dto: {}", dto);

        LoginResult result = memberService.authenticate(dto, request.getSession(), response);
        log.info("result: {}", result);

        // Model에 담긴 데이터는 redirect 시 jsp로 전달되지 못한다.
        // 수명이 짧음 (한번의 요청과 응답 사이에서만 유효)

        ra.addFlashAttribute("result", result);

        if (result == LoginResult.SUCCESS) { // 로그인 성공 시

            // 로그인을 했다는 정보를 계속 유지하기 위한 수단으로 쿠키를 사용하자.
//            makeLoginCookie(dto, response);

            // 세션으로 로그인 유지
            memberService.maintainLoginState(request.getSession(), dto.getAccount());

            return "redirect:/board/list";
        }
        return "redirect:/members/sign-in"; // 로그인 실패 시
    }

    private void makeLoginCookie(LoginRequestDTO dto, HttpServletResponse response) {
        // 쿠키에 로그인 기록을 저장
        // 쿠키 객체를 생성 -> 생성자의 매개값으로 쿠키의 이름과 저장할 값을 전달. (문자열만 저장됨. 용량한계있음)
        Cookie cookie = new Cookie("login", dto.getAccount());

        // 쿠키 세부 설정
        cookie.setMaxAge(60); //쿠키 수명 설정
        cookie.setPath("/"); // 이 쿠키는 모든 경로에서 유효하다.

        // 쿠키가 완성됐다면 응답 객체에 쿠키를 태워서 클라이언트로 전송
        response.addCookie(cookie);

    }

    // 로그아웃 요청 처리
    @GetMapping("/sign-out")
    public String signOut(HttpSession session) {
        log.info("/member/sign-out: GET!");

        // 세션에서 로그인 정보 기록 삭제
        session.removeAttribute("login");

        // 세션 전체 무효화 (초기화)
        session.invalidate();

        return "redirect:/";

    }


}
