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

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    // 로그인 처리
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
//        try {
//            // 입력 값 검사
//            String username = loginRequest.getUsername().trim();
//            String password = loginRequest.getPassword().trim();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"result\":\"failure\",\"message\":\"아이디와 비밀번호는 필수입니다.\"}");
//            }
//
//            // 인증 시도
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            // 인증 성공
//            System.out.println("로그인 성공 - 사용자: " + username);
//            return ResponseEntity.ok("{\"result\":\"success\",\"message\":\"로그인 성공\"}");
//
//        } catch (BadCredentialsException e) {
//            // 아이디 또는 비밀번호가 잘못된 경우
//            System.out.println("로그인 실패 - 잘못된 아이디 또는 비밀번호: " + loginRequest.getUsername());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"result\":\"failure\",\"message\":\"아이디 또는 비밀번호가 잘못되었습니다.\"}");
//        } catch (Exception e) {
//            // 기타 예외 처리
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"result\":\"failure\",\"message\":\"서버 오류가 발생했습니다.\"}");
//        }
//    }




    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRequestDto userRequestDto) {
        UserDto userDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userDto);
    }


    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 로그인 상태 확인
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return ResponseEntity.ok("로그인 상태입니다.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않았습니다.");
    }
}
