package ureca.ureca_miniproject2.post.dto;

public record PostCreateRequest(
        String title,
        String content,
        Integer price,
        String imageUrl,
        Integer userId
) {}
