package ureca.ureca_miniproject2.commoncode;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GroupCode {

    @Id
    private String groupCode;

    private String groupCodeName;

    private String groupCodeDesc;
}
