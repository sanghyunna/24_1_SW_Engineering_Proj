package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
