package ureca.ureca_miniproject2.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.user.dto.LoginRequestDTO;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.service.UserService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRequestDto userRequestDto) {
        UserDto userDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userDto);
    }


    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 세션 무효화
        request.getSession().invalidate();
        return ResponseEntity.ok("logout successful");
    }

    // 로그인 상태 확인
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Map<String, String> response = new HashMap<>();
            response.put("username", authentication.getName());
            response.put("status", "authenticated");
            return ResponseEntity.ok(response);
        }
        Map<String, String> error = new HashMap<>();
        error.put("status", "unauthenticated");
        error.put("message", "로그인되지 않았습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
