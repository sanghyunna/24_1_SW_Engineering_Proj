package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.*;
import cau.se.issuemanagespring.dto.IssueRequest;
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

//    private IssueResponse mapToResponse(Issue issue) {
//        if (issue == null) {
//            return null;
//        }
//        return IssueResponse.builder()
//                .id(issue.getId())
//                .title(issue.getTitle())
//                .reportDate(issue.getReportDate())
//                .updateDate(issue.getUpdateDate())
//                .dueDate(issue.getDueDate())
//                .content(issue.getContent())
//                .reporterName(issue.getReporter().getName())
//                .assigneeNameArray(issue.getAssignee().stream().map(User::getName).collect(Collectors.toList()))
//                .fixerName(issue.getFixer().getName())
//                .statusName(issue.getStatus().name())
//                .priorityName(issue.getPriority().name())
//                .commentIdArray(issue.getComments().stream().map(Comment::getId).collect(Collectors.toList()))
//                .build();
//    }
}
