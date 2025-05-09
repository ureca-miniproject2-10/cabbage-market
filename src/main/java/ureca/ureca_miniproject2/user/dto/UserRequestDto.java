package ureca.ureca_miniproject2.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.user.entity.User;

import java.util.List;

// 등록 수정시 사용하닌 requestDto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String username;       // 아이디
    private String password;       // 비밀번호
    private String name;           // 이름
    private String profileImage;   // 프로필 이미지 (선택)
    private List<String> roles;    // 역할 목록

    // DTO -> Entity 변환
    public User toEntity(UserRequestDto userRequestDto) {
        return User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .name(userRequestDto.getName())
                .profile_image(userRequestDto.getProfileImage())
                .build();
    }

}
