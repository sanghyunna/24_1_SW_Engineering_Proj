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
import cau.se.issuemanagespring.service.UserService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
	
	@Autowired
	private UserService userService;

	@BeforeAll
	public void setUp() {
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll();
		projectRepository.deleteAll();
		userRepository.deleteAll();
		
		UserRequest potter = new UserRequest();
		potter.setName("potter");
		potter.setPassword("1234");
		potter.setToken(null);
		userService.create(potter);
		
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("potter"));
		projectRequest.setDevNameArray(Arrays.asList("potter"));
		projectRequest.setTesterNameArray(Arrays.asList("potter"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "potter");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue1");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("content1");
		issueRequest.setAssigneeNameArray(Arrays.asList("potter"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "potter");
		
		IssueRequest issueRequest_search = new IssueRequest();
		issueRequest_search.setTitle("i'm still hungry");
		issueRequest_search.setDueDate("2024-06-11T12:00:00");
		issueRequest_search.setContent("food content");
		issueRequest_search.setAssigneeNameArray(Arrays.asList("potter"));
		issueRequest_search.setPriority("MAJOR");
		issueRequest_search.setToken(null);
		issueService.create(project_id, issueRequest_search, "potter");
		
	}

	@Test
	@Transactional
    public void testBrowseIssue() {

		
		// 프로젝트 목록에서 이슈를 조회할 프로젝트 선택
		Long project_id = projectService.getAll().get(0).getId();
		
		
		// 이슈목록에서 상세정보를 보고싶은 이슈를 선택
		Long issue_id = issueService.getAll(project_id).get(0).getId();
		
		// 서버는 이슈정보 존재하는지 확인후 응답
		IssueResponse issueResponse = issueService.getById(issue_id);
		
		
		// 테스트 결과 확인
		assertThat(issueResponse.getTitle()).isEqualTo("issue1");
		assertThat(issueResponse.getContent()).isEqualTo("content1");
		assertThat(issueResponse.getPriority()).isEqualTo("MAJOR");
        
	}
	
	@Test
	@Transactional
	public void testBrowseIssueBySearch() {
		
		// 이슈를 검색할 프로젝트 선택
		Long projectId = projectService.getAll().get(0).getId();
				
		// 특정 검색어와 관련된 이슈들를 서버에 요청
		List<IssueResponse> issue_hungry = issueService.search(projectId, "hungry");
				
		// 테스트 결과 확인
		assertThat(issue_hungry.size()).isEqualTo(1);
		assertThat(issue_hungry.get(0).getTitle()).isEqualTo("i'm still hungry");
		
	}
	
	

}
