package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("SELECT i FROM Issue i " +
            "LEFT JOIN i.reporter r " +
            "LEFT JOIN i.fixer f " +
            "WHERE LOWER(COALESCE(i.title, '')) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(COALESCE(r.name, '')) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR EXISTS (SELECT 1 FROM i.assignee ua WHERE LOWER(ua.name) LIKE CONCAT('%', LOWER(:keyword), '%')) " +
            "OR LOWER(COALESCE(f.name, '')) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(i.status) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(i.priority) LIKE CONCAT('%', LOWER(:keyword), '%') ")
    List<Issue> searchIssues(@Param("keyword") String keyword);
}
