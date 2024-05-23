package cau.se.issuemanagespring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IssueRequest {
    private String title;
    private String dueDate;
    private String content;
    private List<String> assigneeNameArray;
    private String priority;
    private String token;
}
