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
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.service.ProjectService;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.dto.CommentResponse;
import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.Priority;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.Project;
import cau.se.issuemanagespring.domain.Status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCommentService {
	
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
        
        User brown_user = new User();
        brown_user.setName("brown");
        userRepository.save(brown_user);
        
        Auth brown_auth = new Auth();
        brown_auth.setUser(brown_user);
        brown_auth.setPassword("1234");
        brown_auth.setToken(null);
        authRepository.save(brown_auth);
        
        Project project_a = new Project();
        project_a.setTitle("project A");
        project_a.setProjectOwner(brown_user);
        project_a.setPLs(Arrays.asList(brown_user));
        project_a.setDevs(Arrays.asList(brown_user));
        project_a.setTesters(Arrays.asList(brown_user));
        project_a = projectRepository.save(project_a);
        
        Issue issue1 = new Issue();
        issue1.setTitle("issue1");
        issue1.setDueDate(LocalDateTime.parse("2024-06-01T12:00:00"));
        issue1.setContent("content1");
        issue1.setProject(project_a);
        issue1.setReporter(brown_user);
        issue1.setAssignee(Arrays.asList(brown_user));
        issue1.setFixer(brown_user);
        issue1.setStatus(Status.ASSIGNED);
        issue1.setPriority(Priority.MAJOR);
        issue1 = issueRepository.save(issue1);
        
        Comment comment1 = new Comment();
        comment1.setContent("comment1");
        comment1.setIssue(issue1);
        comment1.setCommentOwner(brown_user);
        commentRepository.save(comment1);
                
        
	}
	
    @Test
	public void testCreateAndGetAllByIssueId() throws Exception {
    	
    	// given
    	CommentRequest comment2 = new CommentRequest();
        comment2.setContent("comment2");
        comment2.setToken(null);
        
        List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
    	long issueId = issueList.get(0).getId();
        
    	
        // when
        commentService.create(comment2, issueId, "brown");
        List<CommentResponse> commentList = commentService.getAllByIssueId(issueId);
        
        
        
        // then
        assertThat(commentList.size()).isEqualTo(2);
        assertThat(commentList.get(0).getCommentOwner()).isEqualTo("brown");
        assertThat(commentList.get(0).getContent()).isEqualTo("comment1");
        assertThat(commentList.get(1).getCommentOwner()).isEqualTo("brown");
        assertThat(commentList.get(1).getContent()).isEqualTo("comment2");

	}
    
    
    @Test
	public void testUpdate() throws Exception {
    	
		// given
		CommentRequest update_comment = new CommentRequest();
		update_comment.setContent("update_content1");
		
		List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
    	long issueId = issueList.get(0).getId();
    	List<Comment> commentList = commentRepository.findAllByIssueId(issueId);
    	long commentId = commentList.get(0).getId();

		// when
		CommentResponse commentResponse = commentService.update(commentId, update_comment, "brown");

		// then
		assertThat(commentResponse.getContent()).isEqualTo("update_content1");
		assertThat(commentResponse.getCommentOwner()).isEqualTo("brown");
		assertThat(commentResponse.getIssueId()).isEqualTo(issueId);
		
	}
    
    
    
    @Test
	public void testGetCommentResponseList() throws Exception {
    	
    	// given
    	commentRepository.deleteAll();
    	
    	List<Project> projectList = projectRepository.findAll();
    	long projectId = projectList.get(0).getId();
    	List<Issue> issueList = issueRepository.findAllByProjectId(projectId);
    	long issueId = issueList.get(0).getId();
    	
    	Optional<Issue> op_issue1 = issueRepository.findById(issueId);
    	Issue issue1 = op_issue1.get();
    	User brown_user = issue1.getReporter();
    	
    	Comment comment1 = new Comment();
        comment1.setContent("comment1");
        comment1.setIssue(issue1);
        comment1.setCommentOwner(brown_user);
        commentRepository.save(comment1);
    	
    	Comment comment2 = new Comment();
        comment2.setContent("comment2");
        comment2.setIssue(issue1);
        comment2.setCommentOwner(brown_user);
        commentRepository.save(comment2);

    	
    	// when
    	List<Comment> comments = commentRepository.findAllByIssueId(issueId);
    	List<CommentResponse> commentList = commentService.getCommentResponseList(comments);
    	
    	// then
    	assertThat(commentList.size()).isEqualTo(2);
        assertThat(commentList.get(0).getCommentOwner()).isEqualTo("brown");
        assertThat(commentList.get(1).getContent()).isEqualTo("comment2");

	}
    
    
    
    

}