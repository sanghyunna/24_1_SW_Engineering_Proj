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
import cau.se.issuemanagespring.domain.Status;


@SpringBootTest
public class TestAssignDev {
	
	
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
		
		
		User blake_user = new User();
		blake_user.setName("blake");
		userRepository.save(blake_user);
		
		Auth blake_auth = new Auth();
		blake_auth.setUser(blake_user);
		blake_auth.setPassword("1234");
		blake_auth.setToken(null);
		authRepository.save(blake_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("blake"));
		projectRequest.setDevNameArray(Arrays.asList("blake"));
		projectRequest.setTesterNameArray(Arrays.asList("blake"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "blake");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(null);
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "blake");
	}
	
	
	@Test
	@Transactional
	public void test() {
		
		// PL은 ITS에 로그인
		UserRequest userRequest = new UserRequest();
		userRequest.setName("blake");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 생성되어있는 이슈 조회
		Long project_id = projectRepository.findAll().get(0).getId();
		List<Issue> issueList = issueRepository.findAllByProjectId(project_id);
		Long issue_id = issueList.get(0).getId();
		
		// 이슈 상태에 따라 담당 dev가 등록되지 않은 이슈를 검색
		// 이슈에 담당 dev를 등록 요청
		// 서버는 요청을 확인후 이슈의 담당 dev를 등록
		if (issueList.get(0).getStatus() == Status.NEW) {
				
			IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
			issueStatusRequest.setStatusName("ASSIGNED");
			issueStatusRequest.setToken(null);
			issueService.updateStatus(issue_id, issueStatusRequest, "blake");
			
			IssueRequest issueRequest = new IssueRequest();
			issueRequest.setAssigneeNameArray(Arrays.asList("blake"));
			issueService.update(issue_id, issueRequest, "blake");
			
		}
		
		
		// 테스트 결과확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(issueList.get(0).getStatus()).isEqualTo(Status.ASSIGNED);
		assertThat(issueList.get(0).getAssignee().get(0).getName()).isEqualTo("blake");
		
	}

}
