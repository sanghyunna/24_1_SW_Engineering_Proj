package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.*;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueStatusRequest;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    public Issue create(IssueRequest issueRequest, String authUser) {
        if (issueRequest.getTitle() == null || issueRequest.getDueDate() == null || issueRequest.getContent() == null) {
            return null;
        }

        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDueDate(LocalDateTime.parse(issueRequest.getDueDate(), DateTimeFormatter.ISO_DATE_TIME));
        issue.setContent(issueRequest.getContent());
        issue.setReporter(userRepository.findByName(authUser).orElseThrow());

        List<User> users = getUsersByUsernames(issueRequest.getAssigneeNameArray());
        issue.setAssignee(users);

        if (users.isEmpty()) {
            issue.setStatus(Status.NEW);
        }
        else {
            issue.setStatus(Status.ASSIGNED);
        }
        issue.setPriority(Priority.valueOf((issueRequest.getPriorityName() == null) ? "MAJOR" : issueRequest.getPriorityName()));

        return issueRepository.save(issue);
    }

    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    public Issue getById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }

    public Issue update(Long issueId, IssueRequest issueRequest, String authUser) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

//        // issue 작성자 일치 여부 판단
//        if (!Objects.equals(issue.getReporter().getName(), authUser)) {
//            return null;
//        }

        if (issueRequest.getTitle() != null) {
            issue.setTitle(issueRequest.getTitle());
        }
        if (issueRequest.getDueDate() != null) {
            issue.setDueDate(LocalDateTime.parse(issueRequest.getDueDate(), DateTimeFormatter.ISO_DATE_TIME));
        }
        if (issueRequest.getContent() != null) {
            issue.setContent(issueRequest.getContent());
        }
        if (issueRequest.getAssigneeNameArray() != null) {
            if (issue.getAssignee().isEmpty() && issue.getStatus() == Status.NEW) {
                issue.setStatus(Status.ASSIGNED);
            }
            issue.setAssignee(getUsersByUsernames(issueRequest.getAssigneeNameArray()));
        }
        if (issueRequest.getPriorityName() != null) {
            issue.setPriority(Priority.valueOf(issueRequest.getPriorityName()));
        }

        return issueRepository.save(issue);
    }

    public Issue updateStatus(Long issueId, IssueStatusRequest issueStatusRequest, String authUser) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

//        // issue 작성자 일치 여부 판단
//        if (!Objects.equals(issue.getReporter().getName(), authUser)) {
//            return null;
//        }

        if (issueStatusRequest.getStatusName() != null) {
            if (issueStatusRequest.getStatusName().equals("FIXED")) {
                issue.setFixer(userRepository.findByName(authUser).orElseThrow());
            }
            issue.setStatus(Status.valueOf(issueStatusRequest.getStatusName()));
        }

        return issueRepository.save(issue);
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
}
