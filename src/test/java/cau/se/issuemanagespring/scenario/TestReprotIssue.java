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
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.IssueRequest;

@SpringBootTest
public class TestReprotIssue {
	
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
		
        User tester_user = new User();
        tester_user.setName("tester");
        tester_user = userRepository.save(tester_user);
        
        Auth tester_auth = new Auth();
        tester_auth.setUser(tester_user);
        tester_auth.setPassword("1234");
        tester_auth.setToken(null);
        authRepository.save(tester_auth);
        
        ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("tester"));
		projectRequest.setDevNameArray(Arrays.asList("tester"));
		projectRequest.setTesterNameArray(Arrays.asList("tester"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "tester");
	}

	@Test
    public void test() {
		
		// tester는 ITS에 로그인한다
		UserRequest userRequest = new UserRequest();
		userRequest.setName("tester");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 이슈가 발생한 프로젝트에 대해 서버에 이슈 등록을 요청한다
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(Arrays.asList("tester"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		
		// 서버는 요청에 따라 새로운 이슈를 프로젝트에 등록
		// tester를 해당 이슈의 reporter로 설정
		long projectId = projectRepository.findAll().get(0).getId();
		issueService.create(projectId, issueRequest, "tester");
		
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
		assertThat(issueList.get(0).getTitle()).isEqualTo("issue1");
		assertThat(issueList.get(0).getReporter().getName()).isEqualTo("tester");
		
	
	}
        

}
