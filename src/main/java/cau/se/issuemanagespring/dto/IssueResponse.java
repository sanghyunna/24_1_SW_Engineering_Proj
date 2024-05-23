package cau.se.issuemanagespring.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class IssueResponse {
    private Long id;
    private String createDate;
    private String updateDate;
    private String title;
    private String dueDate;
    private String content;
    private Long projectId;
    private String reporter;
    private List<String> assignee;
    private String fixer;
    private String status;
    private String priority;
}
