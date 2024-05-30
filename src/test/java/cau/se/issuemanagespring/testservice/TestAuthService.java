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
            
            
            User nick_user = new User();
            nick_user.setName("nick");
            nick_user = userRepository.save(nick_user);
            
            Auth nick_auth = new Auth();
            nick_auth.setUser(nick_user);
            nick_auth.setPassword("1234");
            nick_auth.setToken("TOKEN_NICK");
            authRepository.save(nick_auth);
            

        }
        
        @Test
        @Transactional
		public void testAuthenticate() {
        	
        	// given
        	
        	// when
        	String name = authService.authenticate("TOKEN_NICK");
        	
        	// then
        	assertThat(name).isEqualTo("nick");

		}
        

        @Test
        @Transactional
        public void testLogin() {
        	
        	// given
        	UserRequest loginRequest1 = new UserRequest();
        	loginRequest1.setName("nick");
        	loginRequest1.setPassword("1234");
        	
        	UserRequest loginRequest2 = new UserRequest();
        	loginRequest2.setName("nick");
        	loginRequest2.setPassword("4321");
        	
        	
        	// when
        	TokenResponse login1 = authService.login(loginRequest1);
        	TokenResponse login2 = authService.login(loginRequest2);
        	
        	// then
        	assertThat(login1.getName()).isEqualTo("nick");
        	assertThat(login2).isNull();
        }


		@Test
		public void testLogout() {
			// given

			// when
			boolean logout = authService.logout("nick");

			// then
			assertThat(logout).isTrue();
		}


}
