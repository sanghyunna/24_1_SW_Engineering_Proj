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
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.ProjectRequest;




@SpringBootTest
public class TestAssignRole {
	
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
        
        User sam_user = new User();
        sam_user.setName("sam");
        sam_user = userRepository.save(sam_user);
        
        Auth sam_auth = new Auth();
        sam_auth.setUser(sam_user);
        sam_auth.setPassword("1234");
        sam_auth.setToken(null);
        authRepository.save(sam_auth);
        
        
        ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("admin"));
		projectRequest.setDevNameArray(Arrays.asList("admin"));
		projectRequest.setTesterNameArray(Arrays.asList("admin"));
		projectRequest.setToken(null);
		projectService.create(projectRequest, "admin");
		
		
	}
	
	@Test
	@Transactional
	public void test() {
		
		// 프로젝트에 초대할 팀원과 역할을 지정
		User sam = userRepository.findByName("sam").orElse(null);
		User admin = userRepository.findByName("admin").orElse(null);
		Long projectId = projectRepository.findAll().get(0).getId();
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project");
		projectRequest.setPLNameArray(Arrays.asList("admin"));
		projectRequest.setDevNameArray(Arrays.asList("admin", "sam"));
		projectRequest.setTesterNameArray(Arrays.asList("admin", "sam"));
		projectRequest.setToken(null);
		
		// 서버에 등록요청을 한다
		// 서버는 요청에 따라 프로젝트를 수정
		// 서버는 프로젝트 팀원의 프로젝트 목록에 초대된 프로젝트를 추가한다
		projectService.update(projectId, projectRequest, "admin");
		
		
		
		// 테스트 결과 확인
		List<Project> projectList = projectRepository.findAll();
		assertThat(projectList.get(0).getProjectOwner().getName()).isEqualTo("admin");
		assertThat(projectList.get(0).getDevs().get(0).getName()).isEqualTo("admin", "sam");
		assertThat(projectList.get(0).getTesters().get(0).getName()).isEqualTo("admin", "sam");
			
	}

}
