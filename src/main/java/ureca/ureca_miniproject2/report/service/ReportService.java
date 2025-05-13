package ureca.ureca_miniproject2.report.service;

public interface ReportService {

    boolean hasReported(Integer userId, Integer postId);

    void reportPost(Integer userId, Integer postId, String content);

}
