package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Issue;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<Issue, Long> {
}
