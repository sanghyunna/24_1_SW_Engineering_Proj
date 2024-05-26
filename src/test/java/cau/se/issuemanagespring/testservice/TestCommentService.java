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
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.dto.ProjectRequest;
import cau.se.issuemanagespring.dto.ProjectResponse;
import cau.se.issuemanagespring.domain.Comment;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


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
        
        
        UserRequest sam = new UserRequest();
        sam.setName("sam");
        sam.setPassword("1234");
        userService.create(sam);
        
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setTitle("project");
        projectRequest.setPLNameArray(Arrays.asList("sam"));
        projectRequest.setDevNameArray(Arrays.asList("james"));
        projectRequest.setTesterNameArray(Arrays.asList("sam", "james"));
        projectRequest.setToken("PJ1_TOKEN");  // 이것도 어디에 쓰이는것인가?
        projectService.create(projectRequest, "sam");
        
        IssueRequest issue = new IssueRequest();
        issue.setTitle("title");
        issue.setDueDate("2024-06-01T12:00:00");
        issue.setContent("content");
        issue.setAssigneeNameArray(Arrays.asList("sam"));
        issue.setPriority("MAJOR");
        issue.setToken("ISSUE_TOKEN");      // 이슈의 토큰은 어디에 사용하는가...?
        issueService.create(3L, issue, "sam");
        
        CommentRequest comment1 = new CommentRequest();
        comment1.setContent("comment1");
        comment1.setToken("COMMENT1_TOKEN"); // 이토큰은 무엇인가..
        commentService.create(comment1, 5L, "sam");
        
        CommentRequest comment2 = new CommentRequest();
        comment2.setContent("comment2");
        comment2.setToken("COMMENT2_TOKEN");
        commentService.create(comment2, 5L, "sam");
                
        
	}
	
    @Test
	public void testCreateAndGetAllByIssueId() throws Exception {
    	
    	// given
    	CommentRequest comment1 = new CommentRequest();
        comment1.setContent("comment3");
        comment1.setToken("COMMENT1_TOKEN"); // 이토큰은 무엇인가..
    	
        // when
        commentService.create(comment1, 5L, "sam");
        List<CommentResponse> commentList1 = commentService.getAllByIssueId(5L);
        
        
        
        // then
        assertThat(commentList1.size()).isEqualTo(3);
        assertThat(commentList1.get(0).getContent()).isEqualTo("comment1");
        assertThat(commentList1.get(0).getCommentOwner()).isEqualTo("sam");
        assertThat(commentList1.get(1).getContent()).isEqualTo("comment2");
        assertThat(commentList1.get(1).getCommentOwner()).isEqualTo("sam");
        assertThat(commentList1.get(2).getContent()).isEqualTo("comment3");
        assertThat(commentList1.get(2).getCommentOwner()).isEqualTo("sam");
        

	}
    
    
    @Test
	public void testUpdate() throws Exception {
    	
		// given
		CommentRequest update_comment = new CommentRequest();
		update_comment.setContent("update_content1");
		update_comment.setToken("UPDATE_TOKEN"); 

		// when
		CommentResponse commentResponse = commentService.update(4L, update_comment, "sam");

		// then
		assertThat(commentResponse.getContent()).isEqualTo("update_content1");
		assertThat(commentResponse.getCommentOwner()).isEqualTo("sam");
		assertThat(commentResponse.getIssueId()).isEqualTo(5L);
		
	}
    
    
    
    @Test
	public void testGetCommentResponseList() throws Exception {
    	
    	// given
    	
    	// when
    	List<Comment> comments = commentRepository.findAllByIssueId(5L);
    	List<CommentResponse> commentList2 = commentService.getCommentResponseList(comments);
    	
    	// then
    	assertThat(commentList2.size()).isEqualTo(3);  // 앞에 테스트에서 하나 추가되서 3개
        assertThat(commentList2.get(0).getCommentOwner()).isEqualTo("sam");
        assertThat(commentList2.get(1).getContent()).isEqualTo("comment2");

	}
    
    
    
    

}