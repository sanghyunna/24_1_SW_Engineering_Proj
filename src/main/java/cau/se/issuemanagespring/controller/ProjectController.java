package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.service.AuthService;
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

    @Autowired
    private AuthService authService;

    /**
     * Project를 생성합니다. 생성된 Project를 반환합니다.
     * @param projectRequest title, PLNameArray, DevNameArray, TesterNameArray, token
     * @return ProjectResponse
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
        // token 검증
        String authUser = authService.authenticate(projectRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectResponse createdProject = projectService.create(projectRequest, authUser);
        if (createdProject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    /**
     * 모든 Project를 반환합니다.
     * @return ProjectResponse의 List
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAll();
        return ResponseEntity.ok().body(projects);
    }

    /**
     * projectId에 해당하는 Project를 반환합니다.
     * @param projectId Project의 ID
     * @return ProjectResponse
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId) {
        ProjectResponse project = projectService.getById(projectId);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    /**
     * projectId에 해당하는 Project를 수정합니다. 수정된 Project를 반환합니다.
     * @param projectId Project의 ID
     * @param projectRequest title, PLNameArray, DevNameArray, TesterNameArray, token(필수)
     * @return ProjectResponse
     */
    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest projectRequest) {
        // token 검증
        String authUser = authService.authenticate(projectRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ProjectResponse updatedProject = projectService.update(projectId, projectRequest, authUser);
        if (updatedProject == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(updatedProject);
    }
}
