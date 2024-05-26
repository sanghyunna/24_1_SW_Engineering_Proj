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
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.dto.TokenRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.service.AuthService;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestAuthService {
	
	    
        
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
        
        @BeforeEach
        public void setUp() {
            authRepository.deleteAll();
            commentRepository.deleteAll();
            issueRepository.deleteAll();
            projectRepository.deleteAll();
            userRepository.deleteAll();
            
            
            UserRequest userRequest = new UserRequest();
            userRequest.setName("sam");
            userRequest.setPassword("1234");
            userRequest.setToken("SAM_TOKEN");
            userService.create(userRequest);

            User sam = userRepository.findByName("sam").orElse(null);
            assertThat(sam).isNotNull();

        }
        
        // userservice 수정필요
        @Test
        @Transactional
		public void testAuthenticate() {
        	// given
        	
        	// when
        	String name = authService.authenticate("SAM_TOKEN");
        	
        	// then
        	assertThat(name).isEqualTo("sam");

		}
        


        //authrepo에 토큰이 저장도 되는건가?
        //로그인의 기능이 무엇인가? 뭘 검증해야하는가?
        @Test
        @Transactional
        public void testLogin() {
        	// given
        	UserRequest loginRequest1 = new UserRequest();
        	loginRequest1.setName("sam");
        	loginRequest1.setPassword("1234");
        	loginRequest1.setToken("SAM_TOKEN");
        	
        	UserRequest loginRequest2 = new UserRequest();
        	loginRequest2.setName("sam");
        	loginRequest2.setPassword("1234");
        	loginRequest2.setToken("TOM_TOKEN");
        	
        	
        	// when
        	TokenResponse login1 = authService.login(loginRequest1);
        	TokenResponse login2 = authService.login(loginRequest2);
        	
        	// then
        	assertThat(login1.getName()).isEqualTo("sam");
        	assertThat(login2.getName()).isEqualTo("sam");
        }


		@Test
		public void testLogout() {
			// given

			// when
			boolean logout = authService.logout("sam");

			// then
			assertThat(logout).isTrue();
		}


}
