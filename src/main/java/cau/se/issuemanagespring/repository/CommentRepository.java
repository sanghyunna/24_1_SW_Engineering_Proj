package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
