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
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.TokenRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.dto.ProjectRequest;






@SpringBootTest
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

	@BeforeEach
	public void setUp() {
		authRepository.deleteAll();
        commentRepository.deleteAll();
        issueRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
		
        User admin_user = new User();
        admin_user.setName("admin");
        admin_user = userRepository.save(admin_user);
        
        Auth admin_auth = new Auth();
        admin_auth.setUser(admin_user);
        admin_auth.setPassword("1234");
        admin_auth.setToken(null);
        authRepository.save(admin_auth);
		
		
	}
	
	@Test
	@Transactional
	public void test() {
		
		// admin은 ITS에 로그인한다
		UserRequest userRequest = new UserRequest();
		userRequest.setName("admin");
		userRequest.setPassword("1234");
		TokenResponse tokenResponse = authService.login(userRequest);
		
		// 프로젝트를 생성을 요청한다
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("new project");
		projectRequest.setPLNameArray(Arrays.asList("admin"));
		projectRequest.setDevNameArray(Arrays.asList("admin"));
		projectRequest.setTesterNameArray(Arrays.asList("admin"));
		projectRequest.setToken(null);
		
		// 서버는 요청을 확인하고 프로젝트를 생성한다
		// 서버는 admin의 프로젝트 목록에 생성된 프로젝트를 추가한다 
		projectService.create(projectRequest, "admin");
		
		
		// 테스트 결과 확인
		assertThat(tokenResponse.getToken()).isNotNull();
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList.get(0).getProjectOwner().getName()).isEqualTo("admin");
		assertThat(projectList.get(0).getTitle()).isEqualTo("new project");
		
	}

	

}
