package cau.se.issuemanagespring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class IssueRequest {
    private String title;
    private String dueDate;
    private String content;
    private String reporterName;
    private List<String> assigneeNameArray;
    private String fixerName;
    private String priorityName;
}
