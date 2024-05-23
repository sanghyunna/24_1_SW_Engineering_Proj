package cau.se.issuemanagespring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectRequest {
    private String title;
    private List<String> PLNameArray;
    private List<String> DevNameArray;
    private List<String> TesterNameArray;
    private String token;
}
