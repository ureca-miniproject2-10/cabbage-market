package ureca.ureca_miniproject2.comment.dto;

import lombok.Builder;
import ureca.ureca_miniproject2.user.entity.User;

@Builder
public record UserSimpleDto(
        Integer userId,
        String username
) {
    public static UserSimpleDto from(User user) {
        return UserSimpleDto.builder()
                .userId(user.getUserId()) // User 엔티티의 ID 필드명에 맞게 수정
                .username(user.getUsername()) // User 엔티티의 사용자명 필드에 맞게 수정
                .build();
    }
}
