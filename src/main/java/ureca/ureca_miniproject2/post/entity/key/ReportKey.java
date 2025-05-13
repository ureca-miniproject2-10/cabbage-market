package ureca.ureca_miniproject2.post.entity.key;

import java.io.Serializable;
import java.util.Objects;

public class ReportKey implements Serializable {

    private Integer userId;

    private Integer postId;


    public ReportKey() {
    }

    public ReportKey(Integer userId, Integer postId) {
        this.userId = userId;
        this.postId = postId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportKey reportKey = (ReportKey) o;
        return Objects.equals(userId, reportKey.userId) && Objects.equals(postId, reportKey.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
