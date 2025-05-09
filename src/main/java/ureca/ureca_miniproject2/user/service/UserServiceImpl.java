package ureca.ureca_miniproject2.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> userList() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::toDto)  // User -> UserDto 변환
                .toList();
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return null;
    }

}
