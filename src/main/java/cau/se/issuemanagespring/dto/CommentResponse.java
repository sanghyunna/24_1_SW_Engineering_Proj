package cau.se.issuemanagespring.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String createDate;
    private String updateDate;
    private String content;
    private Long issueId;
    private String commentOwner;
}
