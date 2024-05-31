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
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.IssueStatusRequest;
import cau.se.issuemanagespring.domain.Issue;


@SpringBootTest
public class TestUpdateIssueStatus {
	
	
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


	@BeforeEach
	public void setUp() {
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
		
		
		User mike_user = new User();
		mike_user.setName("mike");
		mike_user = userRepository.save(mike_user);
		
		Auth mike_auth = new Auth();
		mike_auth.setUser(mike_user);
		mike_auth.setPassword("1234");
		mike_auth.setToken(null);
		authRepository.save(mike_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("mike"));
		projectRequest.setDevNameArray(Arrays.asList("mike"));
		projectRequest.setTesterNameArray(Arrays.asList("mike"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "mike");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(Arrays.asList("mike"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "mike");
	}
	
	
	@Test
	@Transactional
	public void test() {
		
		// primary actor는 ITS에 로그인
		UserRequest userRequest = new UserRequest();
		userRequest.setName("mike");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 생성되어있는 이슈 조회
		Long project_id = projectRepository.findAll().get(0).getId();
		List<Issue> issueList = issueRepository.findAllByProjectId(project_id);
		Long issue_id = issueList.get(0).getId();
		
		// 이슈의 진행상황에 따라 서버에 상태 변경을 요청
		// 서버는 요청을 확인하고 이슈의 상태를 변경
		IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("REOPENED");
		issueStatusRequest.setToken(null);
		issueService.updateStatus(issue_id, issueStatusRequest, "mike");
		
		
		// 테스트 결과확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(issueList.get(0).getStatus()).isEqualTo(Status.REOPENED);
		
	}

}
