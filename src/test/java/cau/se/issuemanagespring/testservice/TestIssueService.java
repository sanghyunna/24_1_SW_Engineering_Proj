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
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.dto.IssueResponse;





import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
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
	
	
	@BeforeEach
	


    @Test
    public void testCreateAndGetAll() throws Exception {

    }
	
	@Test
	public void testGetById() throws Exception {

	}
	
	@Test
	public void testGetIssueCountByPriority() throws Exception {

	}
	
	@Test
	public void testGetIssueCountByStatus() throws Exception {

	}
	
	@Test
	public void testGetIssueResponse() throws Exception {

	}
	
	@Test
	public void testGetIssueResponseList() throws Exception {

	}
	
	@Test
	public void testGetMonthlyIssueCount() throws Exception {

	}
	
	@Test
	public void testGetTotalIssueCount() throws Exception {

	}
	
	@Test
	public void testGetUsersByUsernames() throws Exception {

	}
	
	@Test
	public void testJaccardSimilarity() throws Exception {

	}
	
	@Test
	public void recommendAssignee() throws Exception {

	}
	
	@Test
	public void search() throws Exception {

	}
	
	@Test
	public void update() throws Exception {

	}
	
	@Test
	public void updateStatus() throws Exception {

	}
	
	

}