package ureca.ureca_miniproject2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.entity.UserUserRole;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;
    private String username;
    private String name;
    private String profileImage;
    private List<String> roles;


    public static UserDto toDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getProfile_image(),
                user.getRole().stream()
                        .map(userUserRole -> userUserRole.getUserRole().getRole())
                        .collect(Collectors.toList())
        );
    }

}
