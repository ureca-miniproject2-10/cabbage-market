package ureca.ureca_miniproject2.commoncode;


import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class CodeKey implements Serializable {

    private String code;
    private String groupCode;

    public CodeKey() {
    }

//    public CodeKey(String code, String groupCode) {
//        this.code = code;
//        this.groupCode = groupCode;
//    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CodeKey codeKey = (CodeKey) o;
        return Objects.equals(code, codeKey.code) && Objects.equals(groupCode, codeKey.groupCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, groupCode);
    }
}
