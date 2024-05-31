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
import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.domain.Issue;


@SpringBootTest
public class TestManageComment {
	
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
	private CommentService commentService;

	@BeforeEach
	public void setUp() {
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
		
		User actor_user = new User();
		actor_user.setName("actor");
		actor_user = userRepository.save(actor_user);
		
		Auth actor_auth = new Auth();
		actor_auth.setUser(actor_user);
		actor_auth.setPassword("1234");
		actor_auth.setToken(null);
		authRepository.save(actor_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("actor"));
		projectRequest.setDevNameArray(Arrays.asList("actor"));
		projectRequest.setTesterNameArray(Arrays.asList("actor"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "actor");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(Arrays.asList("actor"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "actor");
	}
	
	
	@Test
	public void test() {
		
		// primary actor는 ITS에 로그인
		UserRequest userRequest = new UserRequest();
		userRequest.setName("actor");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 생성되어있는 이슈 조회
		Long project_id = projectRepository.findAll().get(0).getId();
		List<Issue> issueList = issueRepository.findAllByProjectId(project_id);
		Long issue_id = issueList.get(0).getId();
		
		// 이슈에 comment 등록 요청
		// 서버는 요청을 확인후 comment 등록
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setContent("comment by actor");
		commentRequest.setToken(null);
		commentService.create(commentRequest, issue_id, "actor");
		
		
		// 테스트 결과확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(commentRepository.findAllByIssueId(issue_id).size()).isEqualTo(1);
		assertThat(commentRepository.findAllByIssueId(issue_id).get(0).getContent()).isEqualTo("comment by actor");
		
	}

}