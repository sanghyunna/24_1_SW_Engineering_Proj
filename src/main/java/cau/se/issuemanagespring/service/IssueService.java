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

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    public Issue create(IssueRequest issueRequest) {
        if (issueRequest.getTitle() == null || issueRequest.getDueDate() == null || issueRequest.getContent() == null || issueRequest.getReporterName() == null) {
            return null;
        }

        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDueDate(LocalDateTime.parse(issueRequest.getDueDate(), DateTimeFormatter.ISO_DATE_TIME));
        issue.setContent(issueRequest.getContent());
        issue.setReporter(userRepository.findByName(issueRequest.getReporterName()));
        issue.setAssignee(getUsersByUsernames(issueRequest.getAssigneeNameArray()));

        issue.setFixer(userRepository.findByName(issueRequest.getFixerName()));
        issue.setStatus(Status.valueOf("NEW"));
        issue.setPriority(Priority.valueOf((issueRequest.getPriorityName() == null) ? "MAJOR" : issueRequest.getPriorityName()));

        return issueRepository.save(issue);
    }

    public List<Issue> getAll() {
        return issueRepository.findAll();
    }

    public Issue getById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }

    public Issue update(Long issueId, IssueRequest issueRequest) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

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
            issue.setAssignee(getUsersByUsernames(issueRequest.getAssigneeNameArray()));
            // 이슈 상태 new -> assigned 변경하는 로직 필요
        }
        if (issueRequest.getFixerName() != null) {
            issue.setFixer(userRepository.findByName(issueRequest.getFixerName()));
        }
        if (issueRequest.getPriorityName() != null) {
            issue.setPriority(Priority.valueOf(issueRequest.getPriorityName()));
        }

        return issueRepository.save(issue);
    }

    public Issue updateStatus(Long issueId, IssueStatusRequest issueStatusRequest) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

        if (issueStatusRequest.getStatusName() != null) {
            // fixed로 바꿨을 때 바꾼 사람을 fixer로 등록하는 로직 필요
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
            User user = userRepository.findByName(username);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }
}
