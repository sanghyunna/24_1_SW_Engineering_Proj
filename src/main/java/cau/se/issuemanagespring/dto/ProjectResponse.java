package cau.se.issuemanagespring.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectResponse {
    private Long id;
    private String createDate;
    private String updateDate;
    private String title;
    private String projectOwner;
    private List<String> PL;
    private List<String> Dev;
    private List<String> Tester;
}
