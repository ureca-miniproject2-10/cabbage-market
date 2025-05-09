package ureca.ureca_miniproject2.user.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.entity.UserRole;
import ureca.ureca_miniproject2.user.entity.UserUserRole;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 조회 테스트")
    void testGetUserById() {
        // Given
        User user = User.builder()
                .userId(1)
                .username("lbk")
                .name("이본규")
                .profile_image("lbk.jpg")
                .roles(new ArrayList<>())
                .build();

        UserRole role = UserRole.builder()
                .roleId(1)
                .role("ROLE_USER")
                .build();

        UserUserRole userUserRole = UserUserRole.builder()
                .user(user)
                .userRole(role)
                .userId(1)
                .roleId(1)
                .build();

        // User에 Role 추가
        user.addRole(role);

        // Mockito Stubbing
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // When
        UserDto foundUser = userService.getUserById(1);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(1);
        assertThat(foundUser.getUsername()).isEqualTo("lbk");
        assertThat(foundUser.getName()).isEqualTo("이본규");
        assertThat(foundUser.getRoles()).contains("ROLE_USER");

        // Verify
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("사용자 등록 테스트")
    void testRegisterUser() {
        // Given
        UserRequestDto userRequestDto = new UserRequestDto("lbk", "password", "이본규", "lbk.jpg", List.of("ROLE_USER"));

        // User 생성
        User user = User.builder()
                .userId(1)
                .username("lbk")
                .name("이본규")
                .profile_image("lbk.jpg")
                .roles(new ArrayList<>())
                .build();

        // 기본 Role 추가
        UserRole role = UserRole.builder()
                .roleId(1)
                .role("ROLE_USER")
                .build();

        UserUserRole userUserRole = UserUserRole.builder()
                .user(user)
                .userRole(role)
                .userId(1)
                .roleId(1)
                .build();

        // User에 Role 추가
        user.getRoles().add(userUserRole);

        // Mockito Stubbing
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // When
        UserDto savedUser = userService.registerUser(userRequestDto);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("lbk");
        assertThat(savedUser.getName()).isEqualTo("이본규");
        assertThat(savedUser.getRoles()).contains("ROLE_USER");

        // Verify
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void testDeleteUser() {
        // Given
        int userId = 1;

        // Mockito Stubbing
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(userId);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    void testUserList() {
        // Given
        User user1 = User.builder()
                .userId(1)
                .username("user1")
                .name("User One")
                .roles(new ArrayList<>())
                .build();

        User user2 = User.builder()
                .userId(2)
                .username("user2")
                .name("User Two")
                .roles(new ArrayList<>())
                .build();

        List<User> userList = List.of(user1, user2);

        // Mockito Stubbing
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        // When
        List<UserDto> users = userService.userList();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        assertThat(users.get(1).getUsername()).isEqualTo("user2");

        // Verify
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("사용자 수정 테스트")
    void testUpdateUser() {
        // Given
        int userId = 1;
        User existingUser = User.builder()
                .userId(userId)
                .username("lbk")
                .name("이본규")
                .profile_image("lbk.jpg")
                .roles(new ArrayList<>())
                .build();

        // 역할 추가
        UserRole role = UserRole.builder()
                .roleId(1)
                .role("ROLE_USER")
                .build();

        UserUserRole userUserRole = UserUserRole.builder()
                .user(existingUser)
                .userRole(role)
                .userId(1)
                .roleId(1)
                .build();

        existingUser.getRoles().add(userUserRole);

        UserRequestDto updateDto = new UserRequestDto("lbk", "password", "Updated Name", "updated.jpg", List.of("ROLE_USER"));

        // Mockito Stubbing
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserDto updatedUser = userService.updateUser(userId, updateDto);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getProfileImage()).isEqualTo("updated.jpg");
        assertThat(updatedUser.getRoles()).contains("ROLE_USER");

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }
}
