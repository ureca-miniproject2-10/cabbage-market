package ureca.ureca_miniproject2.post.entity.key;

import java.io.Serializable;
import java.util.Objects;

public class LikeEntityKey implements Serializable {

    private Integer userId;

    private Integer postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntityKey likeEntityKey = (LikeEntityKey) o;
        return Objects.equals(userId, likeEntityKey.userId) && Objects.equals(postId, likeEntityKey.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
