package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.*;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.dto.IssueStatusRequest;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    public IssueResponse create(Long projectId, IssueRequest issueRequest, String authUser) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return null;
        }
        if (issueRequest.getTitle() == null || issueRequest.getDueDate() == null || issueRequest.getContent() == null) {
            return null;
        }

        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDueDate(LocalDateTime.parse(issueRequest.getDueDate(), DateTimeFormatter.ISO_DATE_TIME));
        issue.setContent(issueRequest.getContent());
        issue.setProject(project);
        issue.setReporter(userRepository.findByName(authUser).orElseThrow());

        List<User> users = getUsersByUsernames(issueRequest.getAssigneeNameArray());
        issue.setAssignee(users);

        if (users.isEmpty()) {
            issue.setStatus(Status.NEW);
        }
        else {
            issue.setStatus(Status.ASSIGNED);
        }
        issue.setPriority(Priority.valueOf((issueRequest.getPriority() == null) ? "MAJOR" : issueRequest.getPriority()));

        return getIssueResponse(issueRepository.save(issue));
    }

    public List<IssueResponse> getAll(Long projectId) {
        List<Issue> issues = issueRepository.findAll()
                .stream()
                .filter(issue -> issue.getProject().getId().equals(projectId))
                .toList();
        return getIssueResponseList(issues);
    }

    public IssueResponse getById(Long id) {
        Issue issue = issueRepository.findById(id).orElse(null);
        if (issue == null) {
            return null;
        }
        return getIssueResponse(issue);
    }

    public long getTodayIssueCount(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return -1;
        }
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);
        return issueRepository.countTodayIssues(startOfDay, endOfDay, projectId);
    }

    public long getMonthlyIssueCount(Long projectId) {
        LocalDateTime now = LocalDateTime.now();
        return issueRepository.countMonthlyIssues(now.getMonthValue(), now.getYear(), projectId);
    }

    public Map<String, Long> getIssueCountByStatus(Long projectId) {
        List<Object[]> result = issueRepository.countByStatus(projectId);
        Map<String, Long> countMap = result.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[1]
                ));

        EnumSet<Status> allStatuses = EnumSet.allOf(Status.class);
        return allStatuses.stream()
                .collect(Collectors.toMap(
                        Status::toString,
                        status -> countMap.getOrDefault(status.toString(), 0L)
                ));
    }

    public Map<String, Long> getIssueCountByPriority(Long projectId) {
        List<Object[]> result = issueRepository.countByPriority(projectId);
        Map<String, Long> countMap = result.stream()
                .collect(Collectors.toMap(
                        arr -> arr[0].toString(),
                        arr -> (Long) arr[1]
                ));

        EnumSet<Priority> allPriorities = EnumSet.allOf(Priority.class);
        return allPriorities.stream()
                .collect(Collectors.toMap(
                        Priority::toString,
                        priority -> countMap.getOrDefault(priority.toString(), 0L)
                ));
    }

    public List<String> recommendAssignee(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

        List<Issue> projectIssues = issueRepository.findAllByProjectId(issue.getProject().getId());
        Issue mostSimilarIssue = null;
        double mostSimilarScore = 0.0;

        for (Issue comparedIssue : projectIssues) {
            if (!Objects.equals(comparedIssue.getId(), issue.getId()) && comparedIssue.getStatus() == Status.FIXED) {
                double similarity = jaccardSimilarity(issue.getContent(), comparedIssue.getContent());
                if (similarity > mostSimilarScore) {
                    mostSimilarScore = similarity;
                    mostSimilarIssue = comparedIssue;
                }
            }
        }

        if (mostSimilarIssue == null) {
            return Collections.emptyList();
        }
        else {
            return mostSimilarIssue.getAssignee().stream().map(User::getName).toList();
        }
    }

    public IssueResponse update(Long issueId, IssueRequest issueRequest, String authUser) {
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
        if (issueRequest.getPriority() != null) {
            issue.setPriority(Priority.valueOf(issueRequest.getPriority()));
        }

        return getIssueResponse(issueRepository.save(issue));
    }

    public List<IssueResponse> search(Long projectId, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }
        List<Issue> issues = issueRepository.searchIssues(keyword)
                .stream()
                .filter(issue -> issue.getProject().getId().equals(projectId))
                .toList();
        return getIssueResponseList(issues);
    }

    public IssueResponse updateStatus(Long issueId, IssueStatusRequest issueStatusRequest, String authUser) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;
        }

        if (issueStatusRequest.getStatusName() != null) {
            if (issueStatusRequest.getStatusName().equals(Status.FIXED.toString())) {
                issue.setFixer(userRepository.findByName(authUser).orElseThrow());
            }
            issue.setStatus(Status.valueOf(issueStatusRequest.getStatusName()));
        }

        return getIssueResponse(issueRepository.save(issue));
    }

    public List<User> getUsersByUsernames(List<String> usernames) {
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

    public IssueResponse getIssueResponse(Issue issue) {
        if (issue == null) {
            return null;
        }
        return IssueResponse.builder()
                .id(issue.getId())
                .createDate(issue.getCreateDate().toString())
                .updateDate(issue.getUpdateDate().toString())
                .title(issue.getTitle())
                .dueDate(issue.getDueDate().toString())
                .content(issue.getContent())
                .projectId(issue.getProject().getId())
                .reporter(issue.getReporter().getName())
                .assignee(issue.getAssignee().stream().map(User::getName).collect(Collectors.toList()))
                .fixer(issue.getFixer() != null ? issue.getFixer().getName() : null)
                .status(issue.getStatus().toString())
                .priority(issue.getPriority().toString())
                .build();
    }

    public List<IssueResponse> getIssueResponseList(List<Issue> issues) {
        return issues.stream().map(this::getIssueResponse).collect(Collectors.toList());
    }

    public double jaccardSimilarity(String s1, String s2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(s1.split(" ")));
        Set<String> set2 = new HashSet<>(Arrays.asList(s2.split(" ")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}
