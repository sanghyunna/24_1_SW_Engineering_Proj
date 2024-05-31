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
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;



@SpringBootTest
public class TestSearchIssue {
	
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
		

		User eddy_user = new User();
		eddy_user.setName("eddy");
		eddy_user = userRepository.save(eddy_user);
		
		Auth eddy_auth = new Auth();
		eddy_auth.setUser(eddy_user);
		eddy_auth.setPassword("1234");
		eddy_auth.setToken(null);
		authRepository.save(eddy_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("eddy"));
		projectRequest.setDevNameArray(Arrays.asList("eddy"));
		projectRequest.setTesterNameArray(Arrays.asList("eddy"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "eddy");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("i'm still hungry");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content");
		issueRequest.setAssigneeNameArray(Arrays.asList("eddy"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "eddy");
	}

	@Test
    @Transactional
    public void test() {
		
		// primary actor는 ITS에 로그인
		UserRequest userRequest = new UserRequest();
		userRequest.setName("eddy");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 이슈를 검색할 프로젝트 선택
		Long projectId = projectRepository.findAll().get(0).getId();
		
		// 이슈 정보에 대해 검색어를 작성하고 서버에 요청
		// 요청들어온 정보와 일치하는 이슈를 응답
		List<IssueResponse> issue_hungry = issueService.search(projectId, "hungry");

		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(issue_hungry.size()).isEqualTo(1);
		assertThat(issue_hungry.get(0).getTitle()).isEqualTo("i'm still hungry");
	}
	
	

}
