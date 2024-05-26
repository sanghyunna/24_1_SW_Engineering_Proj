package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.*;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private AuthService authService;

    /**
     * Issue를 생성합니다. 생성된 Issue를 반환합니다.
     * @param projectId Project의 ID
     * @param issueRequest title, dueDate, content, assigneeNameArray, priority, token
     * @return IssueResponse
     */
    @PostMapping("/project/{projectId}/issue")
    public ResponseEntity<IssueResponse> createIssue(@PathVariable("projectId") Long projectId, @RequestBody IssueRequest issueRequest) {
        // token 검증
        String authUser = authService.authenticate(issueRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse createdIssue = issueService.create(projectId, issueRequest, authUser);
        if (createdIssue == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    /**
     * 모든 Issue를 반환합니다.
     * @param projectId Project의 ID
     * @return IssueResponse
     */
    @GetMapping("/project/{projectId}/issue")
    public ResponseEntity<List<IssueResponse>> getAllIssues(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok().body(issueService.getAll(projectId));
    }

    /**
     * keyword로 제목, reporter, assignee, fixer, status, priority를 검색합니다. 검색한 결과를 반환합니다.
     * @param projectId Project의 ID
     * @param keyword 검색 키워드
     * @return IssueResponse의 List
     */
    @GetMapping("/project/{projectId}/issue/search")
    public ResponseEntity<List<IssueResponse>> searchIssues(@PathVariable("projectId") Long projectId, @RequestParam String keyword) {
        return ResponseEntity.ok().body(issueService.search(projectId, keyword));
    }

    /**
     * Issue 통계를 반환합니다.
     * @param projectId Project의 ID
     * @return IssueStatsResponse
     */
    @GetMapping("/project/{projectId}/issue/stats")
    public ResponseEntity<IssueStatsResponse> getIssueStatistics(@PathVariable("projectId") Long projectId) {
        long todayCount = issueService.getTodayIssueCount(projectId);
        if (todayCount == -1) {
            return ResponseEntity.notFound().build();
        }
        long monthlyCount = issueService.getMonthlyIssueCount(projectId);
        Map<String, Long> statusCount = issueService.getIssueCountByStatus(projectId);
        Map<String, Long> priorityCount = issueService.getIssueCountByPriority(projectId);

        IssueStatsResponse dto = new IssueStatsResponse(todayCount, monthlyCount, statusCount, priorityCount);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * issueId에 해당하는 Issue를 반환합니다.
     * @param issueId Issue의 ID
     * @return IssueResponse
     */
    @GetMapping("/issue/{issueId}")
    public ResponseEntity<IssueResponse> getIssueById(@PathVariable("issueId") Long issueId) {
        IssueResponse issue = issueService.getById(issueId);
        if (issue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(issue);
    }

    /**
     * Issue의 내용을 분석해 알맞은 Assignee를 추천합니다. 추천하는 Assignee의 이름 배열이 반환됩니다.
     * @param issueId Issue의 ID
     * @return String의 List
     */
    @GetMapping("/issue/{issueId}/recommend")
    public ResponseEntity<List<String>> getRecommendAssignee(@PathVariable("issueId") Long issueId) {
        List<String> recommendAssignee = issueService.recommendAssignee(issueId);
        if (recommendAssignee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(recommendAssignee);
    }

    /**
     * Issue를 수정합니다. 수정된 Issue를 반환합니다.
     * @param issueId Issue의 ID
     * @param issueRequest title, dueDate, content, assigneeNameArray, priority, token(필수)
     * @return IssueResponse
     */
    @PatchMapping("/issue/{issueId}")
    public ResponseEntity<IssueResponse> updateIssue(@PathVariable("issueId") Long issueId, @RequestBody IssueRequest issueRequest) {
        // token 검증
        String authUser = authService.authenticate(issueRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse updatedIssue = issueService.update(issueId, issueRequest, authUser);
        if (updatedIssue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedIssue);
    }

    /**
     * Issue의 상태를 수정합니다. 수정된 Issue를 반환합니다.
     * @param issueId Issue의 ID
     * @param issueStatusRequest statusName, token(필수)
     * @return IssueResponse
     */
    @PatchMapping("/issue/{issueId}/status")
    public ResponseEntity<IssueResponse> updateIssueStatus(@PathVariable("issueId") Long issueId, @RequestBody IssueStatusRequest issueStatusRequest) {
        // token 검증
        String authUser = authService.authenticate(issueStatusRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        IssueResponse updatedIssue = issueService.updateStatus(issueId, issueStatusRequest, authUser);
        if (updatedIssue == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedIssue);
    }
}
