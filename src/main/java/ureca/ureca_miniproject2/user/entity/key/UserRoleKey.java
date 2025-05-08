package ureca.ureca_miniproject2.user.entity.key;

import java.io.Serializable;
import java.util.Objects;


// 복합키 클래스
public class UserRoleKey implements Serializable {

    private Integer userId;

    private Integer roleId;

    public UserRoleKey() {
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleKey that = (UserRoleKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

}
