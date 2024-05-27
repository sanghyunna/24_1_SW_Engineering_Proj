package cau.se.issuemanagespring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectRequest {
    private String title;
    @JsonProperty("PLNameArray")
    private List<String> PLNameArray;
    @JsonProperty("DevNameArray")
    private List<String> DevNameArray;
    @JsonProperty("TesterNameArray")
    private List<String> TesterNameArray;
    private String token;
}
