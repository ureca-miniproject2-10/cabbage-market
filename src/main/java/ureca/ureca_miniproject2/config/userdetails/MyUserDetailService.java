package ureca.ureca_miniproject2.config.userdetails;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.entity.UserRole;
import ureca.ureca_miniproject2.user.entity.UserUserRole;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("로그인 시도 - username: [" + username + "]");

        // 사용자 조회
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // 사용자 존재 여부 검사
        User user = optionalUser.orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username)
        );

        // 권한 매핑
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(UserUserRole::getUserRole)    // UserUserRole에서 UserRole 가져오기
                .map(UserRole::getRole)            // UserRole에서 역할 이름 가져오기
                .map(SimpleGrantedAuthority::new) // SimpleGrantedAuthority로 변환
                .toList();

        // 권한 정보 출력 (디버그)
        System.out.println("인증된 권한: " + authorities);

        // 사용자 정보 반환 (Spring 기본 User 대신 커스텀 UserDetails 사용)
        return MyUserDetails.builder()
                .userId(user.getUserId())  // 사용자 ID 추가
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .profileImageUrl(user.getProfile_image())
                .authorities(authorities)
                .build();
    }
}

