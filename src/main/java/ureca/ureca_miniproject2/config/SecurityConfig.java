package ureca.ureca_miniproject2.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import ureca.ureca_miniproject2.config.handler.CustomAccessDeniedHandler;
import ureca.ureca_miniproject2.config.handler.MyAuthenticationFailureHandler;
import ureca.ureca_miniproject2.config.handler.MyAuthenticationSuccessHandler;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/", "index.html",
            "/csrf-token",
            "/common/**",
            "/login", "/login.html",
            "/auth/**", "/register.html",
            "/error.html",
            "/posts.html", "/postDetail.html",
            "/navbar.html",
            "/images/**",                // 기본 이미지, 정적 파일
            "/api/posts/*/view",        // 게시글 조회수 증가 API
            "/api/likes/**",            // 좋아요 상태 조회 (GET)
            "/api/reports/**",          // 신고 관련 조회 (GET만 열어둘 경우 확인 필요)
            "/api/posts/**"             // 게시글 전체 조회 API
    };


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 반드시 설정 필요
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            MyAuthenticationSuccessHandler successHandler,
            MyAuthenticationFailureHandler failureHandler,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                // CSRF 토큰을 쿠키에 저장 , 클라이언트가 이를 요청에 포함하도록함
                // ( 상태 변경 메서드 : POST, PUT, PATCH, DELETE 요청에 대해 항상 필요 )
                .csrf(csrf -> csrf
                        .disable())
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)  // 항상 세션 생성
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)  // 세션 고정 보호
                )
                // CORS 설정을 적용
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:8080")); // 해당 url에서 오는 HTTP 요청을 허용
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("X-XSRF-TOKEN"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                // 기본 폼 로그인 비활성화 ( 커스텀 로그인, 회원가입 페이지 사용하기 위해 )
//                .formLogin(form -> form.disable())
                .authorizeHttpRequests(request -> request
                        // [1] 로그인하지 않아도 접근 가능한 공개 경로 (정적 리소스 + 공개 페이지 + 조회 API)
                        // 비회원이 할수있는 것들
                        .requestMatchers(WHITE_LIST).permitAll()

                        // 관리자 전용 페이지 및 API
                        .requestMatchers("/admin/**", "/admin.html", "/api/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        // 나머지 요청은 로그인된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
//                 접근 권한 예외 처리페이지 ( 인가되지 않은 사용자가 보호된 리소스에 접근할때 처리 )
                .exceptionHandling(ex -> ex
                                .accessDeniedHandler(accessDeniedHandler)
                )

                // 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login.html") // 커스텀 로그인 페이지
                        .loginProcessingUrl("/login") // 로그인 처리 URL (POST 요청)
                        .successHandler(successHandler) // 로그인 성공 시 처리
                        .failureHandler(failureHandler) // 로그인 실패 시 처리
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll() // 로그인 페이지는 모든 사용자 접근 허용 -> 위에서 이미 설정됨
                )

                // 로그아웃 설정
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}

