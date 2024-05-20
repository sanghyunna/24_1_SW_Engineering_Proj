package cau.se.issuemanagespring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String name;
    private String token;
}
