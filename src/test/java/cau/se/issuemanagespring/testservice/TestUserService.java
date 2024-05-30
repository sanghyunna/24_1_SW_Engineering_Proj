package cau.se.issuemanagespring.testservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;



import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.AuthRepository;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class TestUserService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private AuthRepository authRepository;
	
	@BeforeEach
	public void setUp() {
	
		authRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll(); // 연관된 issue들을 먼저 삭제해야함
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
		
		Project project_a = new Project();
        project_a.setTitle("project A");
        project_a.setProjectOwner(sam_user);
        project_a.setPLs(Arrays.asList(sam_user));
        project_a.setDevs(Arrays.asList(sam_user));
        project_a.setTesters(Arrays.asList(sam_user));
        project_a = projectRepository.save(project_a);
		
	}
	
	
	

	@Test
	public void testCreateAndGetAll() throws Exception {
		
		// given
		User james_user = new User();
		james_user.setName("james");
		james_user = userRepository.save(james_user);
		
		Auth james_auth = new Auth();
		james_auth.setUser(james_user);
		james_auth.setPassword("1234");
		james_auth.setToken(null);
		authRepository.save(james_auth);
		
		// when
		List<UserResponse> Users = userService.getAll();

		// then
		assertThat(Users.get(0).getName()).isEqualTo("sam");
		assertThat(Users.get(1).getName()).isEqualTo("james");

	}
	
	
	
	@Test
	public void testUpdate() throws Exception {

		// given
		User alex_user = new User();
		alex_user.setName("alex");
		alex_user = userRepository.save(alex_user);
		
		Auth alex_auth = new Auth();
		alex_auth.setUser(alex_user);
		alex_auth.setPassword("1234");
		alex_auth.setToken(null);
		authRepository.save(alex_auth);
		
		UserRequest userRequest = new UserRequest();
		userRequest.setName("new name");
		userRequest.setPassword("4321");
		userRequest.setToken(null);
				
		// when
		UserResponse updatedUser = userService.update(userRequest, "alex");

		// then
		assertThat(updatedUser.getName()).isEqualTo("new name");
		assertThat(updatedUser.getId()).isEqualTo(alex_user.getId());
		
	}
	
	
	
	@Test
	public void testGetUserResponse() throws Exception {
        
		// given
		User jack_user = new User();
		jack_user.setName("jack");
		jack_user = userRepository.save(jack_user);
		
		Auth jack_auth = new Auth();
		jack_auth.setUser(jack_user);
		jack_auth.setPassword("1234");
		jack_auth.setToken(null);
		authRepository.save(jack_auth);

		// when
		UserResponse userResponse = userService.getUserResponse(jack_user);

		// then
	    assertThat(userResponse.getName()).isEqualTo("jack");
	    assertThat(userResponse.getId()).isEqualTo(jack_user.getId());
	}
	
	
	@Test
	public void testGetUserResponseList() throws Exception {
		// given
		User jerry_user = new User();
		jerry_user.setName("jerry");
		jerry_user = userRepository.save(jerry_user);
		
		Auth jerry_auth = new Auth();
		jerry_auth.setUser(jerry_user);
		jerry_auth.setPassword("1234");
		jerry_auth.setToken(null);
		authRepository.save(jerry_auth);
		
		User ben_user = new User();
		ben_user.setName("ben");
		ben_user = userRepository.save(ben_user);
		
		Auth ben_auth = new Auth();
		ben_auth.setUser(ben_user);
		ben_auth.setPassword("1234");
		ben_auth.setToken(null);
		authRepository.save(ben_auth);


		// when
		List<UserResponse> userResponses = userService.getUserResponseList(Arrays.asList(jerry_user, ben_user));

		// then
		assertThat(userResponses.get(0).getName()).isEqualTo("jerry");
		assertThat(userResponses.get(0).getId()).isEqualTo(jerry_user.getId());
		assertThat(userResponses.get(1).getName()).isEqualTo("ben");
		assertThat(userResponses.get(1).getId()).isEqualTo(ben_user.getId());
		
		
	}

}
