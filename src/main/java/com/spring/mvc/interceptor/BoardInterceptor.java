package com.spring.mvc.interceptor;

import com.spring.mvc.util.LoginUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.spring.mvc.util.LoginUtils.*;

// 인터셉터: 컨트롤러에 요청이 들어가기 전, 후에
// 공통적으로 처리할 코드나 검사할 일들을 정의해 놓는 클래스

@Configuration
@Slf4j
public class BoardInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        // 로그인을 안 했다면 글쓰기, 글 수정, 글 삭제 요청을 튕겨낼 것.

        if (!isLogin(session)) {
            log.info("권한 없음! 요청 거부! - {}", request.getRequestURI());
            response.sendRedirect("/members/sign-in");
            return false;
        }
        return true;
    }
}
