package ureca.ureca_miniproject2.post.dto;

import ureca.ureca_miniproject2.post.entity.PostState;

public record PostUpdateRequest(
        String title,
        String content,
        Integer price,
        String imageUrl,
        String state
) {}