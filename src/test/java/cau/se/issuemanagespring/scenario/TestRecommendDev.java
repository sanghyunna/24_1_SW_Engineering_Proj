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
public class TestRecommendDev {
	
	
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
		
		
		User anne_user = new User();
		anne_user.setName("anne");
		userRepository.save(anne_user);
		
		Auth anne_auth = new Auth();
		anne_auth.setUser(anne_user);
		anne_auth.setPassword("1234");
		anne_auth.setToken(null);
		authRepository.save(anne_auth);
		
		User emily_user = new User();
		emily_user.setName("emily");
		userRepository.save(emily_user);
		
		Auth emily_auth = new Auth();
		emily_auth.setUser(emily_user);
		emily_auth.setPassword("1234");
		emily_auth.setToken(null);
		
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("anne"));
		projectRequest.setDevNameArray(Arrays.asList("anne"));
		projectRequest.setTesterNameArray(Arrays.asList("anne"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "anne");
		Long project_id = projectRepository.findAll().get(0).getId();
		
		IssueRequest issueRequest1 = new IssueRequest();
		issueRequest1.setTitle("issue1");
		issueRequest1.setDueDate("2024-06-11T12:00:00");
		issueRequest1.setContent("this issue is for anne");
		issueRequest1.setAssigneeNameArray(Arrays.asList("anne"));
		issueRequest1.setPriority("MAJOR");
		issueRequest1.setToken(null);
		issueService.create(project_id, issueRequest1, "anne");
		
		IssueRequest issueRequest2 = new IssueRequest();
		issueRequest2.setTitle("issue2");
		issueRequest2.setDueDate("2024-06-11T12:00:00");
		issueRequest2.setContent("emily who reported this issue");
		issueRequest2.setAssigneeNameArray(Arrays.asList("emily"));
		issueRequest2.setPriority("MAJOR");
		issueRequest2.setToken(null);
		issueService.create(project_id, issueRequest2, "emily");
		
		Long issue_id1 = issueRepository.findAllByProjectId(project_id).get(0).getId();
		Long issue_id2 = issueRepository.findAllByProjectId(project_id).get(1).getId();
		
		
		IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("FIXED");
		issueStatusRequest.setToken(null);
		issueService.updateStatus(issue_id1, issueStatusRequest, "anne");
		issueService.updateStatus(issue_id2, issueStatusRequest, "emily");
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issue3");
		issueRequest.setDueDate("2024-06-11T12:00:00");
		issueRequest.setContent("assign this issue to emily");
		issueRequest.setAssigneeNameArray(null);
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(project_id, issueRequest, "anne");
	}
	
	
	@Test
	@Transactional
	public void test() {
		
		// PL은 ITS에 로그인
		UserRequest userRequest = new UserRequest();
		userRequest.setName("anne");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 담당자가 지정되지 않은 이슈 조회
		Long project_id = projectRepository.findAll().get(0).getId();
		List<Issue> issueList = issueRepository.findAllByProjectId(project_id);
		Long unassigned_issue_id = 0L;
		for (Issue issue : issueList) {
			if (issue.getStatus() == Status.NEW) {
				unassigned_issue_id = issue.getId();
				break;
			}
		}

		// 해당 이슈에서 통계분석을 사용해 추천 담당자들을 찾음
		// 추천 담당자들 중 한명을 담당자로 지정
		List<String> recommendedAssignees = issueService.recommendAssignee(unassigned_issue_id);
		String recommendedAssignee = recommendedAssignees.get(0);
		
		IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("ASSIGNED");
		issueStatusRequest.setToken(null);
		issueService.updateStatus(unassigned_issue_id, issueStatusRequest, "anne");
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setAssigneeNameArray(Arrays.asList(recommendedAssignee));
		issueService.update(unassigned_issue_id, issueRequest, "anne");

		
		
		// 테스트 결과확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(issueList.get(2).getStatus()).isEqualTo(Status.ASSIGNED);
		assertThat(issueList.get(2).getAssignee().get(0).getName()).isEqualTo("emily");
		
	}

}
