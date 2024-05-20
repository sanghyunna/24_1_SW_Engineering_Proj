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

    public Comment create(CommentRequest commentRequest, Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null || commentRequest.getContent() == null || commentRequest.getCommentOwnerName() == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setIssue(issue);
        comment.setCommentOwner(userRepository.findByName(commentRequest.getCommentOwnerName()));

        return commentRepository.save(comment);
    }

    public List<Comment> getAllByIssueId(Long issueId) {
        return commentRepository.findAllByIssueId(issueId);
    }

    public Comment update(Long issueId, Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || !Objects.equals(comment.getIssue().getId(), issueId)) {
            return null;
        }

        if (comment.getContent() != null) {
            comment.setContent(commentRequest.getContent());
        }

        return commentRepository.save(comment);
    }
}
