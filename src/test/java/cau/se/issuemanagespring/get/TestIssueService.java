package cau.se.issuemanagespring.get;


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


    @Test
    public void testIssueService() throws Exception {
    	
    	
    	//given
    	User sam = new User();
    	sam.setId(1L);
    	sam.setName("sam");
    	
    	User james = new User();
    	james.setId(2L);
    	james.setName("james");
    	
    	userRepository.save(sam);
    	userRepository.save(james);
    	
    	Issue issue1 = new Issue();
    	issue1.setId(3L);    // 기존에 저장된 issue와 같으면 충돌
    	issue1.setTitle("Issue 10");
    	issue1.setStatus(Status.NEW);
    	
    	Issue issue2 = new Issue();
    	issue2.setId(4L);
    	issue2.setTitle("Issue 20");
    	issue2.setStatus(Status.ASSIGNED);
    	
		IssueRequest issueRequest1 = new IssueRequest(
				"Issue 30", 
				LocalDateTime.now().toString(), 
				"Content 30", 
				"sam",
				Arrays.asList("sam", "james"), 
				"james", 
				"BLOCKER"
				);
    	
    	IssueRequest issueRequest2 = new IssueRequest(
    			"Issue 40",
    			LocalDateTime.now().toString(),
    			"Content 40",
    			"sam",
    			Arrays.asList("sam", "james"),
    			"james",
    			"MAJOR"
    			);
    	
    	
    	issueRepository.save(issue1);
    	issueRepository.save(issue2);
    	issueService.create(issueRequest1);
    	issueService.create(issueRequest2);
    	
    	
    	//when
    	List<Issue> result = issueService.getAll();
    	
    	
    	//then
    	assertThat(result).hasSize(6);
        assertThat(result.get(0).getTitle()).isEqualTo("Issue 1");          // 기존에 저장된 issue
        assertThat(result.get(1).getStatus()).isEqualTo(Status.ASSIGNED);   // 기존에 저장된 issue
        
        assertThat(result.get(2).getTitle()).isEqualTo("Issue 10");         // test issue
        assertThat(result.get(3).getStatus()).isEqualTo(Status.ASSIGNED);   // test issue
        assertThat(result.get(4).getFixer().getName()).isEqualTo("james");  // test issue
        assertThat(result.get(5).getReporter().getName()).isEqualTo("sam"); // test issue


    }

}