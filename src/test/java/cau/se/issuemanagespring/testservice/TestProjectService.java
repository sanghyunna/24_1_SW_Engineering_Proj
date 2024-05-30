package cau.se.issuemanagespring.testservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;



import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.dto.UserRequest;




import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestProjectService {
	
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
	private ProjectService projectService;
	
	@BeforeAll
	public void setUp() {
		authRepository.deleteAll();
        commentRepository.deleteAll();
        issueRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
        
        User sam_user = new User();
        sam_user.setName("sam");
        sam_user = userRepository.save(sam_user);
        
        Auth sam_auth = new Auth();
        sam_auth.setUser(sam_user);
        sam_auth.setPassword("1234");
        sam_auth.setToken(null);
        authRepository.save(sam_auth);
        
        User james_user = new User();
		james_user.setName("james");
		james_user = userRepository.save(james_user);
		
		Auth james_auth = new Auth();
		james_auth.setUser(james_user);
		james_auth.setPassword("1234");
		james_auth.setToken(null);
		authRepository.save(james_auth);
		
		Project project_a = new Project();
        project_a.setTitle("project A");
        project_a.setProjectOwner(sam_user);
        project_a.setPLs(Arrays.asList(sam_user));
        project_a.setDevs(Arrays.asList(sam_user));
        project_a.setTesters(Arrays.asList(sam_user, james_user));
        project_a = projectRepository.save(project_a);
        
        Project project_b = new Project();
        project_b.setTitle("project B");
        project_b.setProjectOwner(james_user);
        project_b.setPLs(Arrays.asList(james_user));
        project_b.setDevs(Arrays.asList(james_user));
        project_b.setTesters(Arrays.asList(sam_user, james_user));
        project_b = projectRepository.save(project_b);
               

             
        
	}
	

	@Test
	@Transactional
	public void testCreateAndGetAll() {
        // given
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project C");
		projectRequest.setPLNameArray(Arrays.asList("sam"));
		projectRequest.setDevNameArray(Arrays.asList("sam"));
		projectRequest.setTesterNameArray(Arrays.asList("sam", "james"));
		projectRequest.setToken(null);

		// when
		ProjectResponse project = projectService.create(projectRequest, "sam");
		List<ProjectResponse> projectList = projectService.getAll();
		
		// then
		assertThat(projectList.size()).isEqualTo(3);
		assertThat(projectList.get(0).getTitle()).isEqualTo("project A");
		assertThat(projectList.get(1).getTitle()).isEqualTo("project B");
		assertThat(projectList.get(2).getTitle()).isEqualTo("project C");
		
		
	}
	
	@Test
	@Transactional
	public void testGetById() {
		// given
		Optional<User> op_sam = userRepository.findByName("sam");
		User sam = op_sam.get();
		
		Project project_d = new Project();
		project_d.setTitle("project D");
		project_d.setProjectOwner(sam);
		project_d.setPLs(Arrays.asList(sam));
		project_d.setDevs(Arrays.asList(sam));
		project_d.setTesters(Arrays.asList(sam));
		project_d = projectRepository.save(project_d);
		
		
		// when
		ProjectResponse project = projectService.getById(project_d.getId());

		// then
		assertThat(project.getTitle()).isEqualTo("project D");
		assertThat(project.getProjectOwner()).isEqualTo("sam");
	}
	
	
	
	@Test
	public void testUpdate( ) {
		
		// given
		Optional<User> op_james = userRepository.findByName("james");
		User james = op_james.get();
		
		Project project_e = new Project();
		project_e.setTitle("project E");
		project_e.setProjectOwner(james);
		project_e.setPLs(Arrays.asList(james));
		project_e.setDevs(Arrays.asList(james));
		project_e.setTesters(Arrays.asList(james));
		project_e = projectRepository.save(project_e);
		
		ProjectRequest projectUpdate = new ProjectRequest();
		projectUpdate.setTitle("project_update");
		projectUpdate.setPLNameArray(Arrays.asList("sam"));
		projectUpdate.setDevNameArray(Arrays.asList("sam"));
		projectUpdate.setTesterNameArray(Arrays.asList("sam"));
		projectUpdate.setToken(null);
		
		
		// when
		ProjectResponse updatedProject = projectService.update(project_e.getId(), projectUpdate, "james");
		
		// then
		assertThat(updatedProject.getTitle()).isEqualTo("project_update");
		assertThat(updatedProject.getProjectOwner()).isEqualTo("james");
		assertThat(updatedProject.getPL()).isEqualTo(Arrays.asList("sam"));
		
	}
	
	
	@Test
	@Transactional
	public void testGetProjectResponseList() {
		// given
		List<Project> projects = projectRepository.findAll();
		Project project1 = projects.get(0);
		Project project2 = projects.get(1);

		// when
		List<ProjectResponse> projectList = projectService.getProjectResponseList(Arrays.asList(project1, project2));

		// then
		assertThat(projectList.size()).isEqualTo(2);
		assertThat(projectList.get(0).getTitle()).isEqualTo("project A");
		assertThat(projectList.get(1).getTitle()).isEqualTo("project B");
	}



}
