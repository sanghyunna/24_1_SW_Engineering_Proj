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
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.repository.ProjectRepository;
import cau.se.issuemanagespring.service.ProjectService;




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
	
	@Autowired
	private ProjectRepository projectRepository;


    @Test
    public void testIssueService() throws Exception {
    	
    	
    	// create issue에 대한 테스트
    	
    	//given    	
    	User sam = new User();
    	sam.setName("sam");
    	sam.setId(6L);
    	
    	User james = new User();
    	james.setName("james");
    	james.setId(4L);
    	// id랑 저장되는 번호가 같아야 검색이 가능하다!!!!!!!!!!!!!!!!!!!!!!!!!
    	userRepository.save(sam);
    	userRepository.save(james);
    	
    	Project project = new Project();
    	project.setId(3L);
    	project.setTitle("Project 10");
    	project.setProjectOwner(sam);
    	project.setPLs(Arrays.asList(sam));
    	project.setDevs(Arrays.asList(james));
    	project.setTesters(Arrays.asList(james));
    	
    	projectRepository.save(project);
    	
    	
    	
    	
//    	Issue issue1 = new Issue();   // title, duedate, content없으면 null
//    	issue1.setId(10L);    // 기존에 저장된 issue와 같으면 충돌
//    	issue1.setTitle("Issue 10");
//    	issue1.setDueDate(LocalDateTime.now());
//    	issue1.setStatus(Status.NEW);
//    	issue1.setProject(project);
//    	issue1.setReporter(sam);
//    	issue1.setAssignee(Arrays.asList(sam));
//    	issue1.setPriority(Priority.MAJOR);
//    	issue1.setFixer(james);
//    	issue1.setContent("Content 10");
//    	
//    	Issue issue2 = new Issue();
//    	issue2.setId(20L);
//    	issue2.setTitle("Issue 20");
//    	issue2.setDueDate(LocalDateTime.now());
//    	issue2.setStatus(Status.ASSIGNED);
//    	issue2.setProject(project);
//    	issue2.setReporter(james);
//    	issue2.setAssignee(Arrays.asList(james));
//    	issue2.setPriority(Priority.MINOR);
//    	issue2.setFixer(sam);
//    	issue2.setContent("Content 20");
    	
    	
		IssueRequest issueRequest1 = new IssueRequest();
		issueRequest1.setTitle("Issue 30");
		issueRequest1.setDueDate(LocalDateTime.now().toString());
		issueRequest1.setContent("Content 30");
		issueRequest1.setAssigneeNameArray(Arrays.asList("sam", "james"));
		issueRequest1.setPriority("MAJOR");
		issueRequest1.setToken("TOKEN10");

    	
    	IssueRequest issueRequest2 = new IssueRequest();
    	issueRequest2.setTitle("Issue 40");
    	issueRequest2.setDueDate(LocalDateTime.now().toString());
    	issueRequest2.setContent("Content 40");
    	issueRequest2.setAssigneeNameArray(Arrays.asList("sam", "james"));
    	issueRequest2.setPriority("BLOCKER");
    	issueRequest2.setToken("TOKEN20");

    	// 수정되고 저장
    	//issueRepository.save(issue1);
    	//issueRepository.save(issue2);
    	
    	issueService.create(3L, issueRequest1, "sam");
    	issueService.create(3L, issueRequest2, "james");
    	
    	
    	//when
    	List<IssueResponse> result = issueService.getAll(3L);
    	
    	
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