package cau.se.issuemanagespring.get;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.hamcrest.Matchers.*;

import cau.se.issuemanagespring.controller.IssueController;
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.service.CommentService;
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.domain.Comment;


import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetAllComments {


	private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private IssueController issueController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
    }
    
    
    @Test
    public void testGetAllComments() throws Exception {
    	
    	User sam = new User();
    	sam.setId(1L);
    	sam.setName("sam");
    	Issue issue1 = new Issue();
    	issue1.setId(1L);
    	issue1.setTitle("Issue 1");
    	issue1.setStatus(Status.NEW);
    	
    	
    	Comment comment1 = new Comment();
    	comment1.setCommentOwner(sam);
    	comment1.setIssue(issue1);
    	
    	Comment comment2 = new Comment();
    	comment2.setCommentOwner(sam);
    	comment2.setIssue(issue1);
    	
    	Comment comment3 = new Comment();
    	comment3.setCommentOwner(sam);
    	comment3.setIssue(issue1);
    	
    	
        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
        when(commentService.getAllByIssueId(1L)).thenReturn(comments);

        mockMvc.perform(get("/issue/1/comment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].commentOwner.name", is("sam")))
            .andExpect(jsonPath("$[1].commentOwner.name", is("sam")))
            .andExpect(jsonPath("$[2].commentOwner.id", is(1)))
            .andExpect(jsonPath("$[0].issue.id", is(1)))
            .andExpect(jsonPath("$[1].issue.title", is("Issue 1")))
            .andExpect(jsonPath("$[2].issue.status", is("NEW")));
        
        verify(commentService, times(1)).getAllByIssueId(1L);
    }


	
    
}