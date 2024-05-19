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


import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class GetAllIssues {
	
	
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
    public void testGetAllIssues() throws Exception {
    	
    	Issue issue1 = new Issue();
    	issue1.setId(1L);
    	issue1.setTitle("Issue 1");
    	issue1.setStatus(Status.NEW);
    	
    	Issue issue2 = new Issue();
    	issue2.setId(2L);
    	issue2.setTitle("Issue 2");
    	issue2.setStatus(Status.ASSIGNED);
    	
    	Issue issue3 = new Issue();
    	issue3.setId(3L);
    	issue3.setTitle("Issue 3");
    	issue3.setStatus(Status.CLOSED);
    	
        List<Issue> issues = Arrays.asList(issue1, issue2, issue3);
        when(issueService.getAll()).thenReturn(issues);

        mockMvc.perform(get("/issue"))
            .andExpect(status().isOk())                // https status 200
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)))
            
            .andExpect(jsonPath("$[0].title", is("Issue 1")))
            .andExpect(jsonPath("$[1].title", is("Issue 2")))
            
            .andExpect(jsonPath("$[1].status", is("ASSIGNED")))
            .andExpect(jsonPath("$[2].status", is("CLOSED")))
            
            .andExpect(jsonPath("$", hasSize(3)));


        verify(issueService, times(1)).getAll();
    }

}
