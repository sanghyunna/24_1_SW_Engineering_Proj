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
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.TokenRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.ProjectService;






@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestManageProject {
	
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
	private UserService userService;

	@Autowired
	private AuthService authService;

	@Autowired
	private ProjectService projectService;

	@BeforeAll
	public void setUp() {
		authRepository.deleteAll();
        commentRepository.deleteAll();
        issueRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
		
        UserRequest gon = new UserRequest();
        gon.setName("gon");
        gon.setPassword("1234");
		userService.create(gon);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("gon"));
		projectRequest.setDevNameArray(Arrays.asList("gon"));
		projectRequest.setTesterNameArray(Arrays.asList("gon"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "gon");
		
		
	}
	
	@Test
	@Transactional
	public void testCreateProject() {
		
		// 허가된 사용자가 로그인 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("gon");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 사용자가 프로젝트 정보를 입력한다
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("new project");
		projectRequest.setPLNameArray(Arrays.asList("gon"));
		projectRequest.setDevNameArray(Arrays.asList("gon"));
		projectRequest.setTesterNameArray(Arrays.asList("gon"));
		projectRequest.setToken(null);
		
		// 서버에 프로젝트 생성을 요청한다
		projectService.create(projectRequest, "gon");
		List<ProjectResponse> projectList = projectService.getAll();
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(projectList.size()).isEqualTo(2);
		assertThat(projectList.get(1).getProjectOwner()).isEqualTo("gon");
		assertThat(projectList.get(1).getTitle()).isEqualTo("new project");
		
	}
	
	@Test
	@Transactional
	public void testModifyProject() {
		
		// 허가된 사용자가 로그인 시도
		UserRequest userRequest = new UserRequest();
		userRequest.setName("gon");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 사용자가 수정할 프로젝트를 선택
		long project_id = projectService.getAll().get(0).getId();
		
		// 사용자가 수정할 정보를 입력
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("modified project");
		projectRequest.setPLNameArray(Arrays.asList("gon"));
		projectRequest.setDevNameArray(Arrays.asList("gon"));
		projectRequest.setTesterNameArray(Arrays.asList("gon"));
		projectRequest.setToken(null);
		
		// 서버에 프로젝트 수정을 요청
		ProjectResponse modifiedProject = projectService.update(project_id, projectRequest, "gon");
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		assertThat(modifiedProject.getTitle()).isEqualTo("modified project");
		
	}

	

}
