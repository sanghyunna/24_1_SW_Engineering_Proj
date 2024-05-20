package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project create(ProjectRequest projectRequest) {
        if (projectRequest.getTitle() == null || projectRequest.getOwnerName() == null) {
            return null;
        }

        Project project = new Project();
        project.setTitle(projectRequest.getTitle());
        project.setProjectOwner(userRepository.findByName(projectRequest.getOwnerName()));
        project.setPLs(getUsersByUsernames(projectRequest.getPLNameArray()));
        project.setDevs(getUsersByUsernames(projectRequest.getDevNameArray()));
        project.setTesters(getUsersByUsernames(projectRequest.getTesterNameArray()));

        return projectRepository.save(project);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project update(Long updateId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(updateId).orElse(null);
        if (project == null) {
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

        return projectRepository.save(project);
    }

    public List<User> getUsersByUsernames(List<String> usernames) {
        List<User> users = new ArrayList<>();
        if (usernames == null) {
            return new ArrayList<>();
        }
        for (String username: usernames) {
            User user = userRepository.findByName(username);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }
}
