package cau.se.issuemanagespring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class IssueStatsResponse {
    private long todayCount;
    private long monthlyCount;
    private Map<String, Long> statusCount;
    private Map<String, Long> priorityCount;
}
