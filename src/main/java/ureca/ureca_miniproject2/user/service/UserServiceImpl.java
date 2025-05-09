package ureca.ureca_miniproject2.user.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.entity.UserRole;
import ureca.ureca_miniproject2.user.entity.UserUserRole;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import java.util.List;

// repository entity 꺼내서 -> service dto 처리
// controller 에 DTO 객체 반환
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        User user = userRequestDto.toEntity(userRequestDto);

        user.addRole(new UserRole("ROLE_USER"));

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
