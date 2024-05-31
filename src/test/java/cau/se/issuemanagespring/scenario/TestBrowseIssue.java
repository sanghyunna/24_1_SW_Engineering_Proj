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
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.IssueService;

@SpringBootTest
public class TestBrowseIssue {
	
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
		
		User harry_user = new User();
		harry_user.setName("harry");
		harry_user = userRepository.save(harry_user);
		
		Auth harry_auth = new Auth();
		harry_auth.setUser(harry_user);
		harry_auth.setPassword("1234");
		harry_auth.setToken(null);
		authRepository.save(harry_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("harry"));
		projectRequest.setDevNameArray(Arrays.asList("harry"));
		projectRequest.setTesterNameArray(Arrays.asList("harry"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "harry");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(Arrays.asList("harry"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "harry");
		
	}

	@Test
    @Transactional
    public void test() {
		
		// primary actor는 ITS에 로그인한다
		UserRequest userRequest = new UserRequest();
		userRequest.setName("harry");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 프로젝트 목록에서 이슈를 조회할 프로젝트 선택
		Long project_id = projectRepository.findAll().get(0).getId();
		List<Issue> issueList = issueRepository.findAllByProjectId(project_id);
		Long issue_id = issueList.get(0).getId();
		
		// 이슈목록에서 상세정보를 보고싶은 이슈를 선택하고 서버에 요청
		// 서버는 이슈정보 존재하는지 확인후 응답
		IssueResponse issueResponse = issueService.getById(issue_id);
		
		
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(issueResponse.getTitle()).isEqualTo("issue1");
		assertThat(issueResponse.getContent()).isEqualTo("content1");
		assertThat(issueResponse.getPriority()).isEqualTo("MAJOR");
        
	}

}
