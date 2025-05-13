package ureca.ureca_miniproject2.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ureca.ureca_miniproject2.util.response.ApiResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");

        // 예: ApiResponse 구조에 맞게 에러 응답 반환
        ApiResponse<Object> errorResponse = ApiResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .success(false)
                .message("접근 권한이 없습니다(AccessDeniedHandler)")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
