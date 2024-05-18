package cau.se.issuemanagespring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String content;
    private String commentOwnerName;
}
