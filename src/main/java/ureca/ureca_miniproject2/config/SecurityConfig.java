package ureca.ureca_miniproject2.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
            MyAuthenticationFailureHandler failureHandler
    ) throws Exception {
        http
                // CSRF 토큰을 쿠키에 저장 , 클라이언트가 이를 요청에 포함하도록함
                // ( 상태 변경 메서드 : POST, PUT, PATCH, DELETE 요청에 대해 항상 필요 )
//                .csrf(csrf->csrf.disable())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
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
                .formLogin(form -> form.disable())
                .authorizeHttpRequests( // 인가 (Authorization) 과정 설정
                        request -> request.
                                requestMatchers( // 누구나 접근 가능한 경로
                                "/",
                                "index.html",
                                "/csrf-token",
                                        "/common/**",
                                        "/login",
                                "/login.html",
                                "/auth/**",
                                "/register.html",
                                        "/error.html",
                                        "/api/posts/**",
                                        "/users/**",
                                        "/api/likes/**",
                                        "/api/reports/**",
                                        "/api/posts/*/view", // 조회수 증가
                                        "/posts.html", // 게시글 페이지
                                        "/images/**" // 디폴트 이미지

                                )
                        .permitAll()
                                // 관리자 전용 페이지
                                .requestMatchers("/admin/**", "/admin.html").hasAuthority("ROLE_ADMIN")
                                // 게시글 조회는 모든 유저 접근 허용
                                .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/", "/api/posts/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/posts", "/api/posts/", "/api/posts/**").permitAll()

                                // 좋아요
                                .requestMatchers(HttpMethod.GET, "/api/likes/**").permitAll()

                                // 게시글 등록, 수정, 삭제는 USER와 ADMIN만 허용
//                                .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//                                .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // 작성자 검증 필요
//                                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // 작성자 검증 필요
                                .anyRequest().authenticated()

                )
                // 접근 권한 예외 처리페이지 ( 인가되지 않은 사용자가 보호된 리소스에 접근할때 처리 )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendRedirect("/error.html");
//                        })
//                )

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

