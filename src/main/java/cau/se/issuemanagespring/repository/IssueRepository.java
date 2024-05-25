package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findAllByProjectId(Long projectId);

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

    // 오늘 발생한 Issue 개수 (특정 프로젝트)
    @Query("SELECT COUNT(i) FROM Issue i WHERE i.createDate BETWEEN :startOfDay AND :endOfDay AND i.project.id = :projectId")
    long countTodayIssues(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay, @Param("projectId") Long projectId);

    // 이번달 발생한 Issue 개수 (특정 프로젝트)
    @Query("SELECT COUNT(i) FROM Issue i WHERE MONTH(i.createDate) = :month AND YEAR(i.createDate) = :year AND i.project.id = :projectId")
    long countMonthlyIssues(@Param("month") int month, @Param("year") int year, @Param("projectId") Long projectId);

    // Issue priority 종류에 따른 개수 (특정 프로젝트)
    @Query("SELECT i.priority, COUNT(i) FROM Issue i WHERE i.project.id = :projectId GROUP BY i.priority")
    List<Object[]> countByPriority(@Param("projectId") Long projectId);

    // Issue status 종류에 따른 개수 (특정 프로젝트)
    @Query("SELECT i.status, COUNT(i) FROM Issue i WHERE i.project.id = :projectId GROUP BY i.status")
    List<Object[]> countByStatus(@Param("projectId") Long projectId);
}
