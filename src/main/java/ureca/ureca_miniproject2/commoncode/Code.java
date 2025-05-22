package ureca.ureca_miniproject2.commoncode;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Entity
@Data
@IdClass(CodeKey.class)
public class Code {

    @Id
    private String code;
    @Id
    private String groupCode;

    private String codeName;

}
