package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.dto.IssueRequest;
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

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody IssueRequest issueRequest) {
        Issue createdIssue = issueService.create(issueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok().body(issueService.getAll());
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable("issueId") Long issueId) {
        Issue issue = issueService.getById(issueId);
        if (issue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(issue);
    }

    @PostMapping("/{issueId}/comment")
    public ResponseEntity<Comment> createComment(@PathVariable("issueId") Long issueId, @RequestBody CommentRequest commentRequest) {
        Comment createdComment = commentService.create(commentRequest, issueId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/{issueId}/comment")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("issueId") Long issueId) {
        List<Comment> comments = commentService.getAllByIssueId(issueId);
        return ResponseEntity.ok().body(comments);
    }
}
