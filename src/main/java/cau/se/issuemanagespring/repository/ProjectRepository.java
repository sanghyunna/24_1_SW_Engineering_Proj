package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
