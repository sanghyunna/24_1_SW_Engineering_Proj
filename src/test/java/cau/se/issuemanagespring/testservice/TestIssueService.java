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
import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.dto.IssueStatusRequest;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.Priority;





import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIssueService {
	
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
	private IssueService issueService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ProjectService projectService;
	
	
	@BeforeAll
	public void setUp() {
		
		authRepository.deleteAll();
        commentRepository.deleteAll();
        issueRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
        
        User lily_user = new User();
        lily_user.setName("lily");
        lily_user = userRepository.save(lily_user);
        
        Auth lily_auth = new Auth();
        lily_auth.setUser(lily_user);
        lily_auth.setPassword("1234");
        lily_auth.setToken(null);
        authRepository.save(lily_auth);
        
        Project project_a = new Project();
        project_a.setTitle("project A");
        project_a.setProjectOwner(lily_user);
        project_a.setPLs(Arrays.asList(lily_user));
        project_a.setDevs(Arrays.asList(lily_user));
        project_a.setTesters(Arrays.asList(lily_user));
        project_a = projectRepository.save(project_a);
        
        Issue issue1 = new Issue();
        issue1.setTitle("issue1");
        issue1.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
        issue1.setContent("content1");
        issue1.setProject(project_a);
        issue1.setReporter(lily_user);
        issue1.setAssignee(Arrays.asList(lily_user));
        issue1.setFixer(lily_user);
        issue1.setStatus(Status.ASSIGNED);
        issue1.setPriority(Priority.MAJOR);
        issue1 = issueRepository.save(issue1);
        
        Issue issue2 = new Issue();
        issue2.setTitle("issue2");
        issue2.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
        issue2.setContent("content2");
        issue2.setProject(project_a);
        issue2.setReporter(lily_user);
        issue2.setAssignee(Arrays.asList(lily_user));
        issue2.setFixer(lily_user);
        issue2.setStatus(Status.ASSIGNED);
        issue2.setPriority(Priority.BLOCKER);
        issue2 = issueRepository.save(issue2);
        
        Comment comment1 = new Comment();
        comment1.setContent("comment1");
        comment1.setIssue(issue1);
        comment1.setCommentOwner(lily_user);
        commentRepository.save(comment1);
        
        Comment comment2 = new Comment();
        comment2.setContent("comment2");
        comment2.setIssue(issue2);
        comment2.setCommentOwner(lily_user);
        commentRepository.save(comment2);

        
	}
	


    @Test
    @Transactional
    public void testCreateAndGetAll() throws Exception {
    	
    	// given
    	IssueRequest new_issue = new IssueRequest();
    	new_issue.setTitle("issue3");
    	new_issue.setDueDate("2024-06-01T12:00:00");
    	new_issue.setContent("content");
    	new_issue.setAssigneeNameArray(Arrays.asList("lily"));
    	new_issue.setPriority("MINOR");
    	new_issue.setToken(null);
    	
    	List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	
    	
    	// when
    	issueService.create(projectId, new_issue, "lily");
    	List<IssueResponse> issueList = issueService.getAll(projectId);
    	
    	// then
    	assertThat(issueList.size()).isEqualTo(3);
    	assertThat(issueList.get(0).getTitle()).isEqualTo("issue1");
    	assertThat(issueList.get(0).getPriority()).isEqualTo("MAJOR");
    	assertThat(issueList.get(1).getTitle()).isEqualTo("issue2");
    	assertThat(issueList.get(1).getPriority()).isEqualTo("BLOCKER");
    	assertThat(issueList.get(2).getTitle()).isEqualTo("issue3");
    	assertThat(issueList.get(2).getPriority()).isEqualTo("MINOR");
    	

    }
	
    
	@Test
	@Transactional
	public void testGetById() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
    	long issueId = issueList.get(0).getId();
		
		// when
		IssueResponse issue = issueService.getById(issueId);
		
		// then
		assertThat(issue.getTitle()).isEqualTo("issue1");
		assertThat(issue.getPriority()).isEqualTo("MAJOR");
		assertThat(issue.getAssignee()).isEqualTo(Arrays.asList("lily"));
		

	}
	
	
	@Test
	@Transactional
	public void testGetTodayIssueCount() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		// when
		long todayIssueCount = issueService.getTodayIssueCount(projectId);

		// then
		assertThat(todayIssueCount).isEqualTo(2L);

	}
	
	
	@Test
	@Transactional
	public void testGetMonthlyIssueCount() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		
		// when
		long monthlyIssueCount = issueService.getMonthlyIssueCount(projectId);
		
		// then
		assertThat(monthlyIssueCount).isEqualTo(2L);

	}
	
	
	@Test
	@Transactional
	public void testGetIssueCountByStatus() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		// when
		Map<String, Long> issueCountByStatus = issueService.getIssueCountByStatus(projectId);

		// then
		assertThat(issueCountByStatus.size()).isEqualTo(5);  // {"ASSIGNED"=2L, "CLOSED"=0L, "FIXED"=0L, "NEW"=0L, "REOPENED"=0L}
		assertThat(issueCountByStatus).containsKey("ASSIGNED");
	    assertThat(issueCountByStatus).containsKey("NEW");
		assertThat(issueCountByStatus.get("ASSIGNED")).isEqualTo(2L);
        assertThat(issueCountByStatus.get("REOPENED")).isEqualTo(0L);

	}
	
	
	@Test
	@Transactional
	public void testGetIssueCountByPriority() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		// when
		Map<String, Long> issueCountByPriority = issueService.getIssueCountByPriority(projectId);

		// then
		assertThat(issueCountByPriority.size()).isEqualTo(5);  // 종류가 5개
		assertThat(issueCountByPriority).containsKey("MAJOR");
		assertThat(issueCountByPriority).containsKey("MINOR");
		assertThat(issueCountByPriority.get("MAJOR")).isEqualTo(1L);
		assertThat(issueCountByPriority.get("MINOR")).isEqualTo(0L);
		assertThat(issueCountByPriority.get("BLOCKER")).isEqualTo(1L);

	}
	
	
	@Test  // 이름이 이상함
	public void testGetUsersByUsernames() throws Exception {
		
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
		List<User> userList = issueService.getUsersByUsernames(Arrays.asList("lily", "james"));

		// then
		assertThat(userList.size()).isEqualTo(2);
		assertThat(userList.get(0).getName()).isEqualTo("lily");
		assertThat(userList.get(1).getName()).isEqualTo("james");

	}

	
	@Test
	@Transactional
	public void testGetIssueResponseList() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
		
		// when
		List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
		List<IssueResponse> issueResponseList = issueService.getIssueResponseList(issueList);
		
		// then
		assertThat(issueResponseList.size()).isEqualTo(2);
		assertThat(issueResponseList.get(0).getTitle()).isEqualTo("issue1");
		assertThat(issueResponseList.get(0).getPriority()).isEqualTo("MAJOR");
		assertThat(issueResponseList.get(1).getTitle()).isEqualTo("issue2");
		assertThat(issueResponseList.get(1).getPriority()).isEqualTo("BLOCKER");

	}
	
	
	@Test
	@Transactional
	public void testSearch() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		// when
		List<IssueResponse> issueList = issueService.search(projectId, "issue");

		// then
		assertThat(issueList.size()).isEqualTo(2);  // search by title
		assertThat(issueList.get(0).getTitle()).isEqualTo("issue1");
		assertThat(issueList.get(0).getPriority()).isEqualTo("MAJOR");
		assertThat(issueList.get(1).getTitle()).isEqualTo("issue2");
		assertThat(issueList.get(1).getPriority()).isEqualTo("BLOCKER");

	}
	
	@Test
	@Transactional
	public void testSearchByRepository() throws Exception { // content빼고 검색
	    
		// given
		User alex_user = new User();
		alex_user.setName("alex");
		alex_user = userRepository.save(alex_user);
		
		Auth alex_auth = new Auth();
		alex_auth.setUser(alex_user);
		alex_auth.setPassword("1234");
		alex_auth.setToken(null);
		authRepository.save(alex_auth);
		
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();

		
		Issue issue3 = new Issue();
		issue3.setTitle("specific issue");
		issue3.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
		issue3.setContent("issue with specific keyword");
		issue3.setProject(projectRepository.findById(projectId).orElse(null));
		issue3.setReporter(alex_user);
		issue3.setAssignee(Arrays.asList(alex_user));
		issue3.setFixer(alex_user);
		issue3.setStatus(Status.ASSIGNED);
		issue3.setPriority(Priority.CRITICAL);
		issue3 = issueRepository.save(issue3);
		
		Issue issue4 = new Issue();
		issue4.setTitle("Another Issue");
		issue4.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
		issue4.setContent("another content");
		issue4.setProject(projectRepository.findById(projectId).orElse(null));
		issue4.setReporter(alex_user);
		issue4.setAssignee(Arrays.asList(alex_user));
		issue4.setFixer(alex_user);
		issue4.setStatus(Status.ASSIGNED);
		issue4.setPriority(Priority.TRIVIAL);
		issue4 = issueRepository.save(issue4);

	    
	    // when, then
	    List<Issue> searchResults = issueRepository.searchIssues("specific");
	    assertThat(searchResults.size()).isEqualTo(1);
	    assertThat(searchResults.get(0).getContent()).isEqualTo("issue with specific keyword");
	    
	    List<Issue> searchResultsCaseInsensitive = issueRepository.searchIssues("SPECIFIC"); // 대소문자 상관없네?
	    assertThat(searchResultsCaseInsensitive.size()).isEqualTo(1);
	    assertThat(searchResultsCaseInsensitive.get(0).getContent()).isEqualTo("issue with specific keyword");
	    
	    List<Issue> searchByReporter = issueRepository.searchIssues("lily");
	    assertThat(searchByReporter.size()).isEqualTo(2); 

	    List<Issue> searchByAssignee = issueRepository.searchIssues("tom");
	    assertThat(searchByAssignee.size()).isEqualTo(0); 
	    
	    List<Issue> searchByFixer = issueRepository.searchIssues("alex");
	    assertThat(searchByFixer.size()).isEqualTo(2);

	    List<Issue> searchByStatus = issueRepository.searchIssues("assigned");
	     assertThat(searchByStatus.size()).isEqualTo(4); 

	    List<Issue> searchByPriority1 = issueRepository.searchIssues("major");
	    assertThat(searchByPriority1.size()).isEqualTo(1); 
	    
	    List<Issue> searchByPriority2 = issueRepository.searchIssues("TRIVIAL");
	    assertThat(searchByPriority2.size()).isEqualTo(1); 
	    
	    List<Issue> searchByPriority3 = issueRepository.searchIssues("CRITICAL");
	    assertThat(searchByPriority3.size()).isEqualTo(1); 
	}
	
	
	@Test
	@Transactional
	public void testUpdate() throws Exception {
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
		
		Optional<User> optional_lily = userRepository.findByName("lily");
		User lily = optional_lily.get();
		
		Issue issueToUpdate = new Issue();
		issueToUpdate.setTitle("issueToUpdate");
		issueToUpdate.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
		issueToUpdate.setContent("content");
		issueToUpdate.setProject(projectRepository.findById(projectId).orElse(null));
		issueToUpdate.setReporter(lily);
		issueToUpdate.setAssignee(Arrays.asList(lily));
		issueToUpdate.setFixer(lily);
		issueToUpdate.setStatus(Status.ASSIGNED);
		issueToUpdate.setPriority(Priority.MAJOR);
		issueToUpdate = issueRepository.save(issueToUpdate);
		
		IssueRequest update_issue = new IssueRequest();
		update_issue.setTitle("update_issue");
		update_issue.setDueDate("2024-06-01T12:00:00");
		update_issue.setContent("updated content");
		update_issue.setAssigneeNameArray(Arrays.asList("lily"));
		update_issue.setPriority("CRITICAL");
		update_issue.setToken("ISSUE_TOKEN");
		
		
		// when
		issueService.update(issueToUpdate.getId(), update_issue, "lily");
		IssueResponse updated = issueService.getById(issueToUpdate.getId());
		
		// then
		assertThat(updated.getTitle()).isEqualTo("update_issue");
		assertThat(updated.getContent()).isEqualTo("updated content");
		assertThat(updated.getPriority()).isEqualTo("CRITICAL");
		

	}
	
	
	@Test
	@Transactional
	public void testUpdateStatus() throws Exception {  // hibernate가 관리하는 컬렉션???
		
		// given
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
		
		IssueRequest issueRequest = new IssueRequest();
		issueRequest.setTitle("issueToUpdate");
		issueRequest.setDueDate("2024-06-01T12:00:00");
		issueRequest.setContent("content");
		issueRequest.setAssigneeNameArray(Arrays.asList("lily"));
		issueRequest.setPriority("MAJOR");
		issueRequest.setToken(null);
		issueService.create(projectId, issueRequest, "lily");
		
		IssueStatusRequest issueStatusRequest = new IssueStatusRequest();
		issueStatusRequest.setStatusName("FIXED");
		issueStatusRequest.setToken(null);
		
		List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
		Issue issueUpdated = issueList.get(0);
		
		// when
		issueService.updateStatus(issueUpdated.getId(), issueStatusRequest, "lily");
		IssueResponse updated = issueService.getById(issueUpdated.getId());
		
		
		// then
		assertThat(updated.getStatus()).isEqualTo("FIXED");
		

	}
	
	
	@Test
	public void testJaccardSimilarity() throws Exception {
		
		// given
		String str1 = "issue with specific keyword";
		String str2 = "issue with specific keyword";
		String str3 = "comment with different keyword and content";
		String str4 = "issue that the specific keyword and content";
		
		// when, then
		assertThat(issueService.jaccardSimilarity(str1, str2)).isEqualTo(1.0);
		assertThat(issueService.jaccardSimilarity(str1, str3)).isEqualTo(0.25);
		assertThat(issueService.jaccardSimilarity(str1, str4)).isEqualTo(0.375);
		assertThat(issueService.jaccardSimilarity(str3, str4)).isEqualTo(0.3);

	}
	
	
	@Test
	@Transactional
	public void testRecommendAssignee() throws Exception {
		
		// given
		User jack_user = new User();
		jack_user.setName("jack");
		jack_user = userRepository.save(jack_user);
		
		Auth jack_auth = new Auth();
		jack_auth.setUser(jack_user);
		jack_auth.setPassword("1234");
		jack_auth.setToken(null);
		authRepository.save(jack_auth);
		
		User lucas_user = new User();
		lucas_user.setName("lucas");
		lucas_user = userRepository.save(lucas_user);
		
		Auth lucas_auth = new Auth();
		lucas_auth.setUser(lucas_user);
		lucas_auth.setPassword("1234");
		lucas_auth.setToken(null);
		authRepository.save(lucas_auth);
		
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
    	long issueId = issueList.get(0).getId();
		
		
		Issue issue3 = new Issue();
		issue3.setTitle("issue3");
		issue3.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
		issue3.setContent("content1 has specific keyword");
		issue3.setProject(projectRepository.findById(projectId).orElse(null));
		issue3.setReporter(jack_user);
		issue3.setAssignee(Arrays.asList(jack_user));
		issue3.setFixer(jack_user);
		issue3.setStatus(Status.FIXED);
		issue3.setPriority(Priority.MAJOR);
		issue3 = issueRepository.save(issue3);
		
		Issue issue4 = new Issue();
		issue4.setTitle("issue4");
		issue4.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
		issue4.setContent("content2 has specific keyword");
		issue4.setProject(projectRepository.findById(projectId).orElse(null));
		issue4.setReporter(lucas_user);
		issue4.setAssignee(Arrays.asList(lucas_user));
		issue4.setFixer(lucas_user);
		issue4.setStatus(Status.FIXED);
		issue4.setPriority(Priority.MAJOR);
		issue4 = issueRepository.save(issue4);
		
		
		// when
		List<String> recommendedAssignees = issueService.recommendAssignee(issueId);
		
		// then
		assertThat(recommendedAssignees).isNotNull();
	    assertThat(recommendedAssignees).isEqualTo(Arrays.asList("jack"));

	}

	
}