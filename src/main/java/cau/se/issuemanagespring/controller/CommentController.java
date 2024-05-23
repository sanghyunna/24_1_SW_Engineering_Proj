package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.dto.CommentResponse;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue/{issueId}/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    /**
     * issueId에 해당하는 Issue의 Comment를 작성합니다. 생성된 Comment를 반환합니다.
     * @param issueId Issue의 ID
     * @param commentRequest content, token
     * @return CommentResponse
     */
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable("issueId") Long issueId, @RequestBody CommentRequest commentRequest) {
        // token 검증
        String authUser = authService.authenticate(commentRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentResponse createdComment = commentService.create(commentRequest, issueId, authUser);
        if (createdComment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * issueId에 해당하는 Issue의 모든 Comment를 반환합니다.
     * @param issueId Issue의 ID
     * @return CommentResponse의 List
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("issueId") Long issueId) {
        List<CommentResponse> comments = commentService.getAllByIssueId(issueId);
        return ResponseEntity.ok().body(comments);
    }

    /**
     * issueId에 해당하는 Issue에서 commentId에 해당하는 Comment를 수정합니다. 수정된 Comment를 반환합니다.
     * @param issueId Issue의 ID
     * @param commentId Comment의 ID
     * @param commentRequest content, token
     * @return CommentResponse
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("issueId") Long issueId, @PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        // token 검증
        String authUser = authService.authenticate(commentRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentResponse updatedComment = commentService.update(issueId, commentId, commentRequest, authUser);
        if (updatedComment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedComment);
    }
}
