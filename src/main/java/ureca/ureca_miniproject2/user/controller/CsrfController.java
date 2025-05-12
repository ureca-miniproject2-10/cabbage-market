package ureca.ureca_miniproject2.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// login.html 에서 window.onload event에서 토큰값 요청
@RestController
@RequestMapping("/csrf-token")
public class CsrfController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request) {
        // 세션 강제 생성 (중요)
        HttpSession session = request.getSession(true);
        System.out.println("세션 ID: " + session.getId());

        // CSRF 토큰 가져오기
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // 토큰이 없는 경우 예외 처리
        if (csrfToken == null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "CSRF 토큰이 설정되지 않았습니다.");
            return ResponseEntity.status(403).body(errorMap);
        }

        // CSRF 토큰 반환 (쿠키에 포함)
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("csrfToken", csrfToken.getToken());

        return ResponseEntity.ok(tokenMap);
    }
}




