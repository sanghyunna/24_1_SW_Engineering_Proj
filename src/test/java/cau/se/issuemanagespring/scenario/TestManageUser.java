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
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.UserResponse;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestManageUser {
	
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
	
	@BeforeAll
	@Transactional
	public void setUp() {
		authRepository.deleteAll();
        commentRepository.deleteAll();
        issueRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
        
        UserRequest altman = new UserRequest();
        altman.setName("altman");
        altman.setPassword("1234");
        altman.setToken(null);
        userService.create(altman);
	}
	
	
	@Test
	@Transactional
	public void testCreateUser() {
		
		// 사용자가 자신의 정보를 입력
		UserRequest king = new UserRequest();
		king.setName("king");
		king.setPassword("1234");
		king.setToken(null);
		
	    // 서버에 입력한 정보를 토대로 사용자를 생성을 요청
		UserResponse response = userService.create(king);
		
		// 테스트 결과 확인
		assertThat(response.getName()).isEqualTo("king");
		
	}
	
	@Test
	@Transactional
	public void testModifyUser() {
		
		// 허가된 사용자가 로그인 시도
		UserRequest altman_login1 = new UserRequest();
		altman_login1.setName("altman");
		altman_login1.setPassword("1234");
		TokenResponse tokenResponse1 = authService.login(altman_login1);
		
		// 사용자가 수정할 정보를 입력
        UserRequest altman_update = new UserRequest();
        altman_update.setName("altman");
        altman_update.setPassword("4321");
        
        // 서버에 수정한 정보를 토대로 사용자 수정을 요청
        userService.update(altman_update, "altman");
        
        // 사용자가 다시 로그인 시도
        UserRequest altman_login2 = new UserRequest();
        altman_login2.setName("altman");
        altman_login2.setPassword("4321");
        TokenResponse tokenResponse2 = authService.login(altman_login2);
        
        // 테스트결과 확인
        assertThat(tokenResponse1.getToken()).isNotNull();
        assertThat(tokenResponse2.getToken()).isNotNull();
		
	}
        
        

}
