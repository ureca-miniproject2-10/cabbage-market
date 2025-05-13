package ureca.ureca_miniproject2.user.dto;

import ureca.ureca_miniproject2.post.dto.PostResponse;

import java.util.List;

public record MyPageDto(
        String username,
        String name,
        String profileImageUrl,
        List<PostResponse> posts
) {

    public static MyPageDto of(String username,String name, String profileImageUrl, List<PostResponse> posts) {
        return new MyPageDto(username,name, profileImageUrl, posts);
    }
}
