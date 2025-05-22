package ureca.ureca_miniproject2.commoncode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Integer> {
    Code findByCodeAndGroupCode(String code, String groupCode);

    List<Code> findByGroupCode(String groupCode);
}
