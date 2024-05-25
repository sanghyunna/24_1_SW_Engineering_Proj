package cau.se.issuemanagespring.testservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserResponse;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class TestUserService {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Test
	public void testCreate() throws Exception {
		// given
		UserRequest userRequest1 = new UserRequest();
		userRequest1.setName("sam");
		userRequest1.setPassword("1234");
		userRequest1.setToken("SAM_TOKEN"); // 토큰이 userservice에는 아직

		UserRequest userRequest2 = new UserRequest();
		userRequest2.setName("james");
		userRequest2.setPassword("1234");
		userRequest2.setToken("JAMES_TOKEN"); // 토큰이 서비스에는 아직

		userService.create(userRequest1);
		userService.create(userRequest2);
		
		
		User user3 = new User();
		user3.setName("ABC");
		user3.setId(10L);
		
		User user4 = new User();
		user4.setName("DEF");
		user4.setId(20L);
		
		userRepository.save(user3);
		userRepository.save(user4);
		

		// when
		List<UserResponse> users = userService.getAll();

		// then
		assertThat(users.size()).isEqualTo(9);   // 기존 data.sql에 5
		// 이름이 대문자순 그다음 알파벳순을 정렬이 된다
		assertThat(users.get(7).getName()).isEqualTo("sam"); 
		assertThat(users.get(4).getName()).isEqualTo("james");
		assertThat(users.get(0).getName()).isEqualTo("ABC");
		assertThat(users.get(1).getName()).isEqualTo("DEF");
		//assertThat(users.get(0).getId()).isEqualTo(10L); // 오류
		//assertThat(users.get(1).getId()).isEqualTo(20L); // 오류
		// id를 따로 설정해줘도 저장한 순서대로 id가 정해져버린다, issue와 다르게
		// 여기선 각각 8L 9L로 저장되어있음
	}
	
	
	
	@Test
	public void testGetAll() throws Exception {

		// given
		UserRequest userRequest1 = new UserRequest();
		userRequest1.setName("sam");
		userRequest1.setPassword("1234");
		userRequest1.setToken("SAM_TOKEN"); // 토큰이 userservice에는 아직

		UserRequest userRequest2 = new UserRequest();
		userRequest2.setName("james");
		userRequest2.setPassword("1234");
		userRequest2.setToken("JAMES_TOKEN"); // 토큰이 서비스에는 아직

		userService.create(userRequest1);
		userService.create(userRequest2);
				
				
		User user3 = new User();
		user3.setName("ABC");
		user3.setId(10L);
				
		User user4 = new User();
		user4.setName("DEF");
		user4.setId(20L);
				
		userRepository.save(user3);
		userRepository.save(user4);
				

		// when
		List<UserResponse> users = userService.getAll();

		// then
		assertThat(users.size()).isEqualTo(9);   // 기존 data.sql에 5
		// 이름이 대문자순 그다음 알파벳순을 정렬이 된다
		assertThat(users.get(7).getName()).isEqualTo("sam"); 
		assertThat(users.get(4).getName()).isEqualTo("james");
		assertThat(users.get(0).getName()).isEqualTo("ABC");
		assertThat(users.get(1).getName()).isEqualTo("DEF");
		//assertThat(users.get(0).getId()).isEqualTo(10L); // 오류
		//assertThat(users.get(1).getId()).isEqualTo(20L); // 오류
		// id를 따로 설정해줘도 저장한 순서대로 id가 정해져버린다, issue와 다르게
		// 여기선 각각 8L 9L로 저장되어있음
	}
	
	
	
	@Test
	public void testUpdate() throws Exception {
        
        // update user에 대한 테스트
        
        //
	

}
