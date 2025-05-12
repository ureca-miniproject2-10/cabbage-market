package ureca.ureca_miniproject2.user.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.entity.UserRole;
import ureca.ureca_miniproject2.user.entity.UserUserRole;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.user.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

// repository entity 꺼내서 -> service dto 처리
// controller 에 DTO 객체 반환
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<UserDto> userList() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::toDto)  // User -> UserDto 변환
                .toList();
    }

    @Override
    @Transactional
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDto.toDto(user);
    }


    @Override
    @Transactional
    public UserDto registerUser(UserRequestDto userRequestDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.setPassword(encodedPassword);
        User user = userRequestDto.toEntity(userRequestDto);
        // 암호화된 비밀번호 설정
        user.setPassword(encodedPassword);
        // 기본 역할 (ROLE_USER) 조회
        Optional<UserRole> existingRole = userRoleRepository.findByRole("ROLE_USER");
        UserRole userRole = existingRole.orElseGet(() -> userRoleRepository.save(new UserRole("ROLE_USER")));
        // 역할 추가
        user.addRole(userRole);
        // 사용자 저장
        user = userRepository.save(user);

        return UserDto.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userRequestDto.getName());
        user.setProfile_image(userRequestDto.getProfileImage());
        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

}
