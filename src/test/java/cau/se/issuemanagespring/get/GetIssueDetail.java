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
import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.domain.Status;
import cau.se.issuemanagespring.domain.Priority;
import cau.se.issuemanagespring.domain.User;


import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GetIssueDetail {
	
	
	
	private MockMvc mockMvc;

    @Mock
    private IssueService issueService;

    @InjectMocks
    private IssueController issueController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
    }
    
	
	
	@Test
	public void testGetIssueDetails() throws Exception {
		
		
		User sam = new User();
		sam.setId(1L);
		sam.setName("sam");
		
	    Issue issue = new Issue();
	    issue.setId(1L);
	    issue.setTitle("Detailed Issue");
	    issue.setStatus(Status.NEW);
	    issue.setPriority(Priority.MAJOR);
	    issue.setReporter(sam);
	    issue.setAssignee(Arrays.asList(sam));
	    issue.setFixer(sam);
	    issue.setContent("This is a detailed issue");
	    
	    
	    
	    when(issueService.getById(1L)).thenReturn(issue);

	    mockMvc.perform(get("/issue/1"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.id", is(18)))
	        .andExpect(jsonPath("$.title", is("Detailed Issue")))
	        .andExpect(jsonPath("$.status", is("NEW")))
	        .andExpect(jsonPath("$.priority", is("MAJOR")))
	        .andExpect(jsonPath("$.reporter.name", is("sam")))
	        .andExpect(jsonPath("$.assignee", hasSize(1)))
	        .andExpect(jsonPath("$.fixer.id", is(1)))
	        .andExpect(jsonPath("$.content", is("This is a detailed issue")));
	    	

	    verify(issueService, times(1)).getById(1L);
	}

}
