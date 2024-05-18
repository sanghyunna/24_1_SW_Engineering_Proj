package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
