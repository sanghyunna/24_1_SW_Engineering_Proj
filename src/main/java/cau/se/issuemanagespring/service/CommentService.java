package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment create(CommentRequest commentRequest, Long issueId) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setIssue(issueRepository.findById(issueId).orElseThrow(() -> new EntityNotFoundException("Issue with id " + issueId + " not found")));
        comment.setCommentOwner(userRepository.findByName(commentRequest.getCommentOwnerName()));

        return commentRepository.save(comment);
    }

    public List<Comment> getAllByIssueId(Long issueId) {
        return commentRepository.findAllByIssueId(issueId);
    }
}
