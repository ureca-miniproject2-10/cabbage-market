package ureca.ureca_miniproject2.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.post.entity.Report;
import ureca.ureca_miniproject2.post.entity.key.ReportKey;

public interface ReportRepository extends JpaRepository<Report, ReportKey> {
}
