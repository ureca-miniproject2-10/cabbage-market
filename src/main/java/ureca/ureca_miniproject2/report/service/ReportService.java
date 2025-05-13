package ureca.ureca_miniproject2.report.service;


import ureca.ureca_miniproject2.report.dto.ReportResponse;

import java.util.List;

public interface ReportService {

    boolean hasReported(Integer userId, Integer postId);

    void reportPost(Integer userId, Integer postId, String content);


    List<ReportResponse> getAllReports();
}
