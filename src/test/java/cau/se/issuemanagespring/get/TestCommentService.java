package cau.se.issuemanagespring.get;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.dto.CommentRequest;
import cau.se.issuemanagespring.repository.CommentRepository;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import cau.se.issuemanagespring.domain.Comment;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.User;


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

    
    @Test
    public void testCommentService() throws Exception {
    	
    	
    	//given
    	Issue issue = new Issue();
    	issue.setId(10L);             // 기존 issue와 같은 id로 설정하면 충돌
    	issue.setTitle("big issue");
    	issue.setStatus(Status.NEW);
    	issueRepository.save(issue);
    	
    	
    	User sam = new User();
    	sam.setId(10L);
    	sam.setName("sam");
    	userRepository.save(sam);
    	
    	//CommentRequest commentRequest1 = new CommentRequest("comment 1", "sam"); // @AllArgsConstructor??
    	
    	Comment comment1 = new Comment();
    	comment1.setId(1L);
    	comment1.setContent("comment 1");
    	comment1.setIssue(issue);
    	comment1.setCommentOwner(sam);
    	
    	Comment comment2 = new Comment();
    	comment2.setId(2L);
    	comment2.setContent("comment 2");
    	comment2.setIssue(issue);
    	comment2.setCommentOwner(sam);
    	
    	commentRepository.save(comment1);
    	commentRepository.save(comment2);
    	
    	
    	
    	//when
//    	List<Comment> result = commentService.getAllByIssueId(10L);
    	
    	
    	
    	// issueID 맞춰서 다시 수정
    	//then
//    	assertThat(result).hasSize(2);
//    	assertThat(result.get(0).getContent()).isEqualTo(comment1.getContent());
//    	assertThat(result.get(1).getContent()).isEqualTo(comment2.getContent());
//    	assertThat(result.get(0).getCommentOwner()).isEqualTo(sam);
//    	assertThat(result.get(1).getCommentOwner()).isEqualTo(sam);
        


    }

}