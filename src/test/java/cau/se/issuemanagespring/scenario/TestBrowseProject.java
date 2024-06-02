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
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.dto.ProjectResponse;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBrowseProject {
	
	
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
		
		UserRequest lauv = new UserRequest();
		lauv.setName("lauv");
		lauv.setPassword("1234");
		userService.create(lauv);
		
		ProjectRequest projectRequest1 = new ProjectRequest();
		projectRequest1.setTitle("project1 by lauv");
		projectRequest1.setPLNameArray(Arrays.asList("lauv"));
		projectRequest1.setDevNameArray(Arrays.asList("lauv"));
		projectRequest1.setTesterNameArray(Arrays.asList("lauv"));
		projectRequest1.setToken(null);
		projectService.create(projectRequest1, "lauv");
		
		ProjectRequest projectRequest2 = new ProjectRequest();
		projectRequest2.setTitle("project2 by lauv");
		projectRequest2.setPLNameArray(Arrays.asList("lauv"));
		projectRequest2.setDevNameArray(Arrays.asList("lauv"));
		projectRequest2.setTesterNameArray(Arrays.asList("lauv"));
		projectRequest2.setToken(null);
		projectService.create(projectRequest2, "lauv");
		
		
	}
	
	@Test
	@Transactional
	public void testBrowseProject() {
		
		
		// 서버에게 등록되어 있는 프로젝트 리스트를 요청
		List<ProjectResponse> projectList = projectService.getAll();
		
		
		// 서버에게 특정 프로젝트에 대한 정보를 요청
		ProjectResponse project1 = projectService.getById(projectList.get(0).getId());
		ProjectResponse project2 = projectService.getById(projectList.get(1).getId());
		
		// 테스트	결과 확인
		assertThat(projectList.size()).isEqualTo(2);
		assertThat(projectList.get(0).getTitle()).isEqualTo("project1 by lauv");
		assertThat(projectList.get(1).getTitle()).isEqualTo("project2 by lauv");
		assertThat(project1.getTitle()).isEqualTo("project1 by lauv");
		assertThat(project2.getTitle()).isEqualTo("project2 by lauv");
		
		
		
	}
	

}
