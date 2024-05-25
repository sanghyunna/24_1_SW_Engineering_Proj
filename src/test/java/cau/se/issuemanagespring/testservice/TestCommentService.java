package cau.se.issuemanagespring.testservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.UserService;
import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.CommentResponse;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class TestCommentService {
	
	@Autowired
    private CommentService commentService;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

    
    @Test
    public void testCommentService() throws Exception {
    	
    	
    	// create comment에 대한 테스트
    	
    	//given
    	Issue issue = new Issue();
    	issue.setId(2L);             // 기존 issue와 같은 id로 설정하면 충돌
    	issue.setTitle("big issue");
    	issue.setStatus(Status.NEW);
    	issueRepository.save(issue);
    	
    	
    	
    	User sam = new User();
    	sam.setId(5L);   // 여기서 설정해도 user는 순서대로***
    	sam.setName("sam");
    	userRepository.save(sam);
    	
    	
    	Comment comment1 = new Comment();
    	comment1.setId(10L);
    	comment1.setContent("comment 1");
    	comment1.setIssue(issue);
    	comment1.setCommentOwner(sam);
    	
    	Comment comment2 = new Comment();
    	comment2.setId(20L);
    	comment2.setContent("comment 2");
    	comment2.setIssue(issue);
    	comment2.setCommentOwner(sam);
    	
    	commentRepository.save(comment1);
    	commentRepository.save(comment2);
    	
    	
    	CommentRequest commentRequest1 = new CommentRequest();
    	commentRequest1.setContent("comment 3");
    	commentRequest1.setToken("COMMENT_3");
    	
    	CommentRequest commentRequest2 = new CommentRequest();
    	commentRequest2.setContent("comment 4");
    	commentRequest2.setToken("COMMENT_4");
    	
    	// userid가 자동부여되는데, 위에 등록한 id와 실제 id가 다르면 create 오류생김
    	// **************************************************
    	// issueid는 또 안그러네..
    	commentService.create(commentRequest1, 2L, "sam");
    	commentService.create(commentRequest2, 2L, "sam");
    	
    	
    	//when
    	List<CommentResponse> result = commentService.getAllByIssueId(2L);
    	
    	
    	
    	// issueID 맞춰서 다시 수정
    	//then
    	assertThat(result).hasSize(5); 
    	//assertThat(result.get(0).getContent()).isEqualTo(comment1.getContent());
    	//기존database에 2로 저장된 comment가 먼저 나옴, issue가 없어도 comment가 존재함
    	assertThat(result.get(1).getContent()).isEqualTo(comment1.getContent());
    	assertThat(result.get(2).getContent()).isEqualTo("comment 2");
    	//assertThat(result.get(3).getCommentOwner()).isEqualTo(sam);
    	//이런식으로하면 오류, 메모리주소 비교 실패
    	assertThat(result.get(3).getCommentOwner()).isEqualTo("sam");
    	assertThat(result.get(4).getIssueId()).isEqualTo(2L);
        


    }

}