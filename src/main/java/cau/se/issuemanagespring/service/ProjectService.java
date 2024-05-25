package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ProjectResponse create(ProjectRequest projectRequest, String authUser) {
        if (projectRequest.getTitle() == null) {
            return null;
        }

        Project project = new Project();
        project.setTitle(projectRequest.getTitle());
        project.setProjectOwner(userRepository.findByName(authUser).orElseThrow());
        project.setPLs(getUsersByUsernames(projectRequest.getPLNameArray()));
        project.setDevs(getUsersByUsernames(projectRequest.getDevNameArray()));
        project.setTesters(getUsersByUsernames(projectRequest.getTesterNameArray()));

        return getProjectResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> getAll() {
        return getProjectResponseList(projectRepository.findAll());
    }

    public ProjectResponse getById(Long id) {
        return getProjectResponse(projectRepository.findById(id).orElse(null));
    }

    public ProjectResponse update(Long updateId, ProjectRequest projectRequest, String authUser) {
        Project project = projectRepository.findById(updateId).orElse(null);
        if (project == null) {
            return null;
        }

        // project 작성자 일치 여부 판단
        if (!Objects.equals(project.getProjectOwner().getName(), authUser)) {
            return null;
        }

        if (projectRequest.getTitle() != null) {
            project.setTitle(projectRequest.getTitle());
        }
        if (projectRequest.getPLNameArray() != null) {
            project.setPLs(getUsersByUsernames(projectRequest.getPLNameArray()));
        }
        if (projectRequest.getDevNameArray() != null) {
            project.setDevs(getUsersByUsernames(projectRequest.getDevNameArray()));
        }
        if (projectRequest.getTesterNameArray() != null) {
            project.setTesters(getUsersByUsernames(projectRequest.getTesterNameArray()));
        }

        return getProjectResponse(projectRepository.save(project));
    }

    private List<User> getUsersByUsernames(List<String> usernames) {
        List<User> users = new ArrayList<>();
        if (usernames == null) {
            return new ArrayList<>();
        }
        for (String username: usernames) {
            User user = userRepository.findByName(username).orElse(null);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public ProjectResponse getProjectResponse(Project project) {
        if (project == null) {
            return null;
        }
        return ProjectResponse.builder()
                .id(project.getId())
                .createDate(project.getCreateDate().toString())
                .updateDate(project.getUpdateDate().toString())
                .title(project.getTitle())
                .projectOwner(project.getProjectOwner().getName())
                .PL(project.getPLs().stream().map(User::getName).collect(Collectors.toList()))
                .Dev(project.getDevs().stream().map(User::getName).collect(Collectors.toList()))
                .Tester(project.getTesters().stream().map(User::getName).collect(Collectors.toList()))
                .build();
    }

    public List<ProjectResponse> getProjectResponseList(List<Project> projects) {
        return projects.stream().map(this::getProjectResponse).collect(Collectors.toList());
    }
}
