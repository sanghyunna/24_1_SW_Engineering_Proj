package cau.se.issuemanagespring.scenario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.dto.IssueStatusRequest;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestManageIssue {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuthService authService;

	@Autowired
	private IssueService issueService;
	
	@Autowired
	private UserService userService;

	@BeforeAll
	@Transactional
	public void setUp() {
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
		

		UserRequest ian = new UserRequest();
		ian.setName("ian");
		ian.setPassword("1234");
		userService.create(ian);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("ian"));
		projectRequest.setDevNameArray(Arrays.asList("ian"));
		projectRequest.setTesterNameArray(Arrays.asList("ian"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "ian");
		long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest1 = new IssueRequest();
		issueRequest1.setTitle("issue not assigned");
    	issueRequest1.setDueDate("2024-06-11T12:00:00");
    	issueRequest1.setContent("ian can fix this issue");
    	issueRequest1.setAssigneeNameArray(null);
    	issueRequest1.setPriority("MINOR");
    	issueRequest1.setToken(null);
    	issueService.create(project_id, issueRequest1, "ian");
    	
    	IssueRequest issueRequest2 = new IssueRequest();
    	issueRequest2.setTitle("issue assigned");
    	issueRequest2.setDueDate("2024-06-11T12:00:00");
    	issueRequest2.setContent("ian has fixed this issue");
    	issueRequest2.setAssigneeNameArray(Arrays.asList("ian"));
    	issueRequest2.setPriority("MAJOR");
    	issueRequest2.setToken(null);
    	issueService.create(project_id, issueRequest2, "ian");
    	
    	IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("FIXED");
		issueStatusRequest.setToken(null);
		
		long issue_fixed = issueRepository.findAll().get(1).getId();
		issueService.updateStatus(issue_fixed, issueStatusRequest, "ian");
		
		IssueRequest issueRequest3 = new IssueRequest();
		issueRequest3.setTitle("issue to be modified");
		issueRequest3.setDueDate("2024-06-11T12:00:00");
		issueRequest3.setContent("content to be modified");
		issueRequest3.setAssigneeNameArray(Arrays.asList("ian"));
		issueRequest3.setPriority("MAJOR");
		issueRequest3.setToken(null);
		issueService.create(project_id, issueRequest3, "ian");

	}
	
	
	@Test
	@Transactional
	public void testCreateIssue() {
		
		// 허가된 사용자만 생성가능
		// 허가된 사용자가 로그인을 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("ian");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 사용자가 이슈에 대한 정보를 입력
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue_title");
		issueRequest.setDueDate("2024-06-01T12:00:00");
		issueRequest.setContent("issue_content");
		issueRequest.setAssigneeNameArray(Arrays.asList("ian"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		
		// 서버에 해당 이슈 생성 요청
		long project_id = projectRepository.findAll().get(0).getId();
		IssueResponse issueCreated = issueService.create(project_id, issueRequest, "ian");
		
		// 테스트 결과 확인
		assertThat(tokenResponse).isNotNull();
		assertThat(issueCreated.getTitle()).isEqualTo("issue_title");
		assertThat(issueCreated.getContent()).isEqualTo("issue_content");
		
		
	}
	
	@Test
	@Transactional
	public void testModifyIssue() {

		// 이슈 작성자만 수정가능
		// 허가된 사용자가 로그인을 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("ian");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 수정할 이슈를 선택
		long issue_id = issueRepository.findAll().get(2).getId();
		

		// 사용자가 이슈의 수정사항을 입력
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue_fixed");
		issueRequest.setDueDate("2024-06-01T12:00:00");
		issueRequest.setContent("fixed_content");
		issueRequest.setAssigneeNameArray(Arrays.asList("ian"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);

		// 서버에 해당 이슈 수정 요청
		IssueResponse issueModified = issueService.update(issue_id, issueRequest, "ian");
		
		// 테스트 결과 확인
		assertThat(tokenResponse).isNotNull();
		assertThat(issueModified.getTitle()).isEqualTo("issue_fixed");
		assertThat(issueModified.getContent()).isEqualTo("fixed_content");

	}
	
	@Test
	@Transactional
	public void testRecommendDevToIssue() {
		
		// 허가된 사용자가 로그인을 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("ian");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 담당자가 지정되지 않은 이슈 조회
		long project_id = projectRepository.findAll().get(0).getId();
		List<IssueResponse> issueList = issueService.getAll(project_id);
		IssueResponse not_assigned = null;
		for (IssueResponse issue : issueList) {
			if (issue.getStatus() == "NEW") {
				not_assigned = issue;
				break;
			}
		}
		
		// 통계분석으로 해당 이슈의 추천 담당자 목록 생성
		List<String> recommendList = issueService.recommendAssignee(not_assigned.getId());
		
		// 추천 담당자 중 한명을 담당자로 지정
		IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("ASSIGNED");
		issueStatusRequest.setToken(null);
		issueService.updateStatus(not_assigned.getId(), issueStatusRequest, "ian");
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setAssigneeNameArray(recommendList);
		issueService.update(not_assigned.getId(), issueRequest, "ian");
		
		// 테스트 결과 확인
		assertThat(tokenResponse).isNotNull();
		assertThat(recommendList).isEqualTo(Arrays.asList("ian"));
		assertThat(issueService.getById(not_assigned.getId()).getAssignee()).isEqualTo(Arrays.asList("ian"));
		
	}
	


}
