package ureca.ureca_miniproject2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import ureca.ureca_miniproject2.util.response.ApiResponse;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication

    ) throws IOException, ServletException {


        // 로그인한 사용자 정보 꺼내기
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        // ajax로 로그인 성공의 결과를 return
        response.setStatus(HttpServletResponse.SC_OK); //200
        response.setContentType("application/json");
        String jsonStr = """
				{"result" : "success"}
				""";
        response.getWriter().write(jsonStr);
        response.sendRedirect("/index.html");

    }

}
