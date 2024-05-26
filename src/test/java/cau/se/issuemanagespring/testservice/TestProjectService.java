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
        
        UserRequest user1 = new UserRequest();
        user1.setName("sam");
        user1.setPassword("1234");
        user1.setToken("SAM_TOKEN");
        userService.create(user1);
        
        UserRequest user2 = new UserRequest();
        user2.setName("james");
        user2.setPassword("1234");
        user2.setToken("JAMES_TOKEN");
        userService.create(user2);
               
        ProjectRequest projectRequest1 = new ProjectRequest();
        projectRequest1.setTitle("project1");
        projectRequest1.setPLNameArray(Arrays.asList("sam"));
        projectRequest1.setDevNameArray(Arrays.asList("james"));
        projectRequest1.setTesterNameArray(Arrays.asList("sam", "james"));
        projectRequest1.setToken("PJ1_TOKEN");
        projectService.create(projectRequest1, "sam");
        
        ProjectRequest projectRequest2 = new ProjectRequest();
		projectRequest2.setTitle("project2");
		projectRequest2.setPLNameArray(Arrays.asList("james"));
		projectRequest2.setDevNameArray(Arrays.asList("sam"));
		projectRequest2.setTesterNameArray(Arrays.asList("sam", "james"));
		projectRequest2.setToken("PJ2_TOKEN");
		projectService.create(projectRequest2, "james");
             
        
	}
	
	// id가 다른이유는 beforeeach마다 데이터를 다시저장하면서 id다 다시부여됌
	@Test
	@Transactional
	public void testCreateAndGetAll() {
        // given
		
		// when
		List<ProjectResponse> projectList = projectService.getAll();
		
		// then
		assertThat(projectList.size()).isEqualTo(2);
		assertThat(projectList.get(0).getTitle()).isEqualTo("project1");
		assertThat(projectList.get(0).getId()).isEqualTo(3L);
		assertThat(projectList.get(0).getProjectOwner()).isEqualTo("sam");
		assertThat(projectList.get(0).getTester().size()).isEqualTo(2);
		assertThat(projectList.get(1).getTitle()).isEqualTo("project2");
		assertThat(projectList.get(1).getId()).isEqualTo(4L);
		assertThat(projectList.get(1).getProjectOwner()).isEqualTo("james");
		assertThat(projectList.get(1).getTester().size()).isEqualTo(2);
		
	}
	
	@Test
	@Transactional
	public void testGetById() {
		// given
		
		// when
		ProjectResponse project = projectService.getById(3L);

		// then
		assertThat(project.getTitle()).isEqualTo("project1");
		assertThat(project.getProjectOwner()).isEqualTo("sam");
		assertThat(project.getTester().size()).isEqualTo(2);
	}
	
	
	
	@Test
	public void testUpdate( ) {
		// given	
		ProjectRequest projectUpdate = new ProjectRequest();
		projectUpdate.setTitle("project2_update");
		projectUpdate.setPLNameArray(Arrays.asList("sam"));
		projectUpdate.setDevNameArray(Arrays.asList("james"));
		projectUpdate.setTesterNameArray(Arrays.asList("sam", "james"));
		projectUpdate.setToken("PJ2_TOKEN");
		
		
		// when
		ProjectResponse updatedProject = projectService.update(4L, projectUpdate, "james");
		
		// then
		assertThat(updatedProject.getTitle()).isEqualTo("project2_update");
		assertThat(updatedProject.getProjectOwner()).isEqualTo("james");
		assertThat(updatedProject.getId()).isEqualTo(4L);
		
	}
	
	
	@Test
	@Transactional
	public void testGetProjectResponseList() {
		// given
		Project project1 = projectRepository.findById(3L).orElse(null);
		Project project2 = projectRepository.findById(4L).orElse(null);

		// when
		List<ProjectResponse> projectList = projectService.getProjectResponseList(Arrays.asList(project1, project2));

		// then
		assertThat(projectList.size()).isEqualTo(2);
		assertThat(projectList.get(0).getTitle()).isEqualTo("project1");
		assertThat(projectList.get(0).getId()).isEqualTo(3L);
		assertThat(projectList.get(0).getProjectOwner()).isEqualTo("sam");
		assertThat(projectList.get(0).getTester().size()).isEqualTo(2);
		assertThat(projectList.get(1).getId()).isEqualTo(4L);
		assertThat(projectList.get(1).getProjectOwner()).isEqualTo("james");
		assertThat(projectList.get(1).getTester().size()).isEqualTo(2);
	}



}
