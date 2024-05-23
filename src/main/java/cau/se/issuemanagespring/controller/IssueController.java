package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.*;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(@RequestBody IssueRequest issueRequest) {
        // token 검증
        String authUser = authService.authenticate(issueRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse createdIssue = issueService.create(issueRequest, authUser);
        if (createdIssue == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponse>> getAllIssues() {
        return ResponseEntity.ok().body(issueService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<IssueResponse>> searchIssues(@RequestParam String keyword) {
        return ResponseEntity.ok().body(issueService.search(keyword));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponse> getIssueById(@PathVariable("issueId") Long issueId) {
        IssueResponse issue = issueService.getById(issueId);
        if (issue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(issue);
    }

    @PatchMapping("/{issueId}")
    public ResponseEntity<IssueResponse> updateIssue(@PathVariable("issueId") Long issueId, @RequestBody IssueRequest issueRequest) {
        // token 검증
        String authUser = authService.authenticate(issueRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse updatedIssue = issueService.update(issueId, issueRequest, authUser);
        if (updatedIssue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedIssue);
    }

    @PatchMapping("/{issueId}/status")
    public ResponseEntity<IssueResponse> updateIssueStatus(@PathVariable("issueId") Long issueId, @RequestBody IssueStatusRequest issueStatusRequest) {
        // token 검증
        String authUser = authService.authenticate(issueStatusRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse updatedIssue = issueService.updateStatus(issueId, issueStatusRequest, authUser);
        if (updatedIssue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedIssue);
    }

    @PostMapping("/{issueId}/comment")
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

    @GetMapping("/{issueId}/comment")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable("issueId") Long issueId) {
        List<CommentResponse> comments = commentService.getAllByIssueId(issueId);
        return ResponseEntity.ok().body(comments);
    }

    @PatchMapping("/{issueId}/comment/{commentId}")
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
