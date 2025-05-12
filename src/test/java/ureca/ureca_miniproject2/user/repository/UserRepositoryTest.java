package ureca.ureca_miniproject2.user.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ureca.ureca_miniproject2.user.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    // given
    // when
    // then

    @BeforeAll
    void setup() {

        userRepository.deleteAll();

        User user1 = User.builder()
                .username("lbk")
                .password("1234")
                .profile_image("lbk.jpg")
                .name("이본규")
                .build();

        User user2 = User.builder()
                .username("jhs")
                .password("1234")
                .profile_image("jhs.jpg")
                .name("장현서")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    void testSaveUser() {
        User user = User.builder()
                .username("aaa")
                .password("1234")
                .profile_image("aaa.jpg")
                .name("철수")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("aaa");
        assertThat(savedUser.getName()).isEqualTo("철수");
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    void testFindUserByUsername() {
        User user = userRepository.findByUsername("lbk").get();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("lbk");
        assertThat(user.getName()).isEqualTo("이본규");
    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void testDeleteUser() {
        User user = userRepository.findByUsername("lbk").get();
        userRepository.delete(user);

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
    }


}