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
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.domain.Issue;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	@BeforeAll
	@Transactional
	public void setUp() {
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
		
		User ron_user = new User();
		ron_user.setName("ron");
		ron_user = userRepository.save(ron_user);
		
		Auth ron_auth = new Auth();
		ron_auth.setUser(ron_user);
		ron_auth.setPassword("1234");
		ron_auth.setToken(null);
		authRepository.save(ron_auth);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project by ron");
		projectRequest.setPLNameArray(Arrays.asList("ron"));
		projectRequest.setDevNameArray(Arrays.asList("ron"));
		projectRequest.setTesterNameArray(Arrays.asList("ron"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "ron");
		long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1 by ron");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1 by ron");
		issueRequest.setAssigneeNameArray(Arrays.asList("ron"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "ron");
		long issue_id = issueRepository.findAll().get(0).getId();
		
		CommentRequest comment = new CommentRequest();
        comment.setContent("comment1 by ron");
        comment.setToken(null);
        commentService.create(comment, issue_id, "ron");
	}
	
	
	@Test
	@Transactional
	public void testCreateComment() {
		
		// 허가된 사용자가 로그인 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("ron");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 서버에 코멘트를 남길 이슈를 요청
		long project_id = projectRepository.findAll().get(0).getId();
		long issue_id = issueRepository.findAll().get(0).getId();
		
		// 사용자가 코멘트의 정보를 입력
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setContent("new comment by ron");
		commentRequest.setToken(null);
		
		// 서버에 코멘트 생성을 요청
		commentService.create(commentRequest, issue_id, "ron");
		
		
		// 테스트 결과확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(commentRepository.findAllByIssueId(issue_id).size()).isEqualTo(2);
		assertThat(commentRepository.findAllByIssueId(issue_id).get(1).getContent()).isEqualTo("new comment by ron");
		
	}
	
	
	@Test
	@Transactional
	public void testModifyComment() {
		
		// 허가된 사용자가 로그인 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("ron");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 서버에 수정할 코멘트 요청
		long issue_id = issueRepository.findAll().get(0).getId();
		long comment_id = commentRepository.findAll().get(0).getId();
		
		// 사용자가 코멘트의 수정사항 입력
		CommentRequest commentRequest = new CommentRequest();
		commentRequest.setContent("modified comment by ron");
		commentRequest.setToken(null);
		
		// 서버에 코멘트 수정을 요청
		commentService.update(comment_id, commentRequest, "ron");
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(commentRepository.findAllByIssueId(issue_id).get(0).getContent()).isEqualTo("modified comment by ron");
	}

}
