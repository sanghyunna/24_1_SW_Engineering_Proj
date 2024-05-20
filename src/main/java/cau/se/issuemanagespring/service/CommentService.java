package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment create(CommentRequest commentRequest, Long issueId, String authUser) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null || commentRequest.getContent() == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setIssue(issue);
        comment.setCommentOwner(userRepository.findByName(authUser).orElseThrow());

        return commentRepository.save(comment);
    }

    public List<Comment> getAllByIssueId(Long issueId) {
        return commentRepository.findAllByIssueId(issueId);
    }

    public Comment update(Long issueId, Long commentId, CommentRequest commentRequest, String authUser) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || !Objects.equals(comment.getIssue().getId(), issueId)) {
            return null;
        }

        // comment 작성자 일치 여부 판단
        if (!Objects.equals(comment.getCommentOwner().getName(), authUser)) {
            return null;
        }

        if (comment.getContent() != null) {
            comment.setContent(commentRequest.getContent());
        }

        return commentRepository.save(comment);
    }
}
