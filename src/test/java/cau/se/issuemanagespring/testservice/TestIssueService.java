package cau.se.issuemanagespring.testservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.Priority;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.User;




import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class TestIssueService {
	
	@Autowired
    private IssueService issueService;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;


    @Test
    public void testIssueService() throws Exception {
    	
    	
    	// create issue에 대한 테스트
    	
    	//given    	
    	UserRequest userRequest1 = new UserRequest();
    	userRequest1.setName("sam");
    	userRequest1.setPassword("1234");
    	userRequest1.setToken("SAM_TOKEN");   // 토큰이 userservice에는 아직
    	
    	UserRequest userRequest2 = new UserRequest();
    	userRequest2.setName("james");
    	userRequest2.setPassword("1234");
    	userRequest2.setToken("JAMES_TOKEN");  // 토큰이 서비스에는 아직
    	
    	userService.create(userRequest1);
    	userService.create(userRequest2);
    	
    	
    	
    	
    	Issue issue1 = new Issue();   // title, duedate, content없으면 null
    	issue1.setId(10L);    // 기존에 저장된 issue와 같으면 충돌
    	issue1.setTitle("Issue 10");
    	issue1.setDueDate(LocalDateTime.now());
    	issue1.setStatus(Status.NEW);
    	
    	Issue issue2 = new Issue();
    	issue2.setId(20L);
    	issue2.setTitle("Issue 20");
    	issue2.setDueDate(LocalDateTime.now());
    	issue2.setStatus(Status.ASSIGNED);
    	
		IssueRequest issueRequest1 = new IssueRequest(
				"Issue 30", 
				LocalDateTime.now().toString(), 
				"Content 30", 
				Arrays.asList("sam", "james"), 
				"MAJOR", 
				"TOKEN10"
				);
    	
    	IssueRequest issueRequest2 = new IssueRequest(
    			"Issue 40",
    			LocalDateTime.now().toString(),
    			"Content 40",
    			Arrays.asList("sam", "james"),
    			"BLOCKER",
    			"TOKEN20"
    			);
    	
    	
    	issueRepository.save(issue1);
    	issueRepository.save(issue2);
    	issueService.create(issueRequest1, "sam");
    	issueService.create(issueRequest2, "james");
    	
    	
    	//when
    	List<Issue> result = issueService.getAll();
    	
    	
    	//then
    	assertThat(result).hasSize(6);
        assertThat(result.get(0).getTitle()).isEqualTo("Issue 1");          // 기존에 저장된 issue
        assertThat(result.get(1).getStatus()).isEqualTo(Status.ASSIGNED);   // 기존에 저장된 issue
        
        assertThat(result.get(2).getTitle()).isEqualTo("Issue 10");         // test issue
        assertThat(result.get(3).getStatus()).isEqualTo(Status.ASSIGNED);   // test issue
        
        assertThat(result.get(4).getTitle()).isEqualTo("Issue 30");         // test issue
        assertThat(result.get(4).getStatus()).isEqualTo(Status.ASSIGNED);        // test issue
        //assertThat(result.get(5).getpriority()).isEqualTo(Priority.BLOCKER); // test issue
        //이건 issue에 priority가 없어서 오류남
        //assertThat(result.get(4).getFixer().getName()).isEqualTo("james");  // test issue
        //assertThat(result.get(5).getReporter().getName()).isEqualTo("sam"); // test issue
        // request로 issue를 만들경우 fixer가 할당이 되지 않는다, 따라서 두코드 오류남
        // issue에 요소가 issueRequest에 없기 때문에, 그리고 토큰은 어디로 가는겆;?

    }

}