package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest projectRequest) {
        Project createdProject = projectService.create(projectRequest);
        if (createdProject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAll();
        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Long projectId) {
        Project project = projectService.getById(projectId);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest projectRequest) {
        Project updatedProject = projectService.update(projectId, projectRequest);
        if (updatedProject == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedProject);
    }
}
