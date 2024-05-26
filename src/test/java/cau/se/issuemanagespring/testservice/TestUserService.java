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
	private AuthRepository AuthRepository;
	
	@BeforeEach
	public void setUp() {
		AuthRepository.deleteAll();
		commentRepository.deleteAll();
		issueRepository.deleteAll(); // 연관된 issue들을 먼저 삭제해야함
		projectRepository.deleteAll();
		userRepository.deleteAll();
		
		UserRequest userRequest = new UserRequest();
		userRequest.setName("sam");
		userRequest.setPassword("1234");
		userRequest.setToken("SAM_TOKEN");
		userService.create(userRequest);
		
		ProjectRequest projectRequest = new ProjectRequest();
		projectRequest.setTitle("project1");
		projectRequest.setPLNameArray(Arrays.asList("sam"));
		projectRequest.setDevNameArray(Arrays.asList("sam"));
		projectRequest.setTesterNameArray(Arrays.asList("sam"));
		projectService.create(projectRequest, "sam");
	}
	
	
	

	@Test
	public void testCreateAndGetAll() throws Exception {
		// given
		UserRequest userRequest = new UserRequest();
		userRequest.setName("james");
		userRequest.setPassword("1234");
		userRequest.setToken("JAMES_TOKEN");
		userService.create(userRequest);
		

		// when
		List<UserResponse> createdUser = userService.getAll();

		// then
		// userid는 정렬후 부여, 1순위: 대문자, 2순위: 알파벳순
		assertThat(createdUser.get(0).getName()).isEqualTo("james");
		assertThat(createdUser.get(0).getId()).isEqualTo(7L);        // id부여방식?
		assertThat(createdUser.get(1).getName()).isEqualTo("sam");
		assertThat(createdUser.get(1).getId()).isEqualTo(6L);

	}
	
	
	
	@Test
	public void testUpdate() throws Exception {

		// given
		User user = new User();
		user.setName("tom");
		user.setId(10L);
		userRepository.save(user);
		
		UserRequest userRequest = new UserRequest();
		userRequest.setName("new name");
		userRequest.setPassword("4321");
		userRequest.setToken("NEW_TOKEN");
				
		// when
		UserResponse updatedUser = userService.update(10L, userRequest);

		// then
		assertThat(updatedUser.getName()).isEqualTo("new name");
		assertThat(updatedUser.getId()).isEqualTo(10L);
		
	}
	
	
	
	@Test
	public void testGetUserResponse() throws Exception {
        
		// given
		User user = new User();
		user.setName("tom");
		user.setId(10L);
		

		// when
		UserResponse userResponse = userService.getUserResponse(user);

		// then
	    assertThat(userResponse.getName()).isEqualTo("tom");
	    assertThat(userResponse.getId()).isEqualTo(10L);
	}
	
	
	@Test
	public void testGetUserResponseList() throws Exception {
		// given
		User user1 = new User();
		user1.setName("tom");
		user1.setId(10L);
		
		User user2 = new User();
		user2.setName("jerry");
		user2.setId(20L);


		// when
		List<UserResponse> userResponses = userService.getUserResponseList(Arrays.asList(user1, user2));

		// then
		assertThat(userResponses.get(0).getName()).isEqualTo("tom");
		assertThat(userResponses.get(0).getId()).isEqualTo(10L);
		assertThat(userResponses.get(1).getName()).isEqualTo("jerry");
		assertThat(userResponses.get(1).getId()).isEqualTo(20L);
	}

}
