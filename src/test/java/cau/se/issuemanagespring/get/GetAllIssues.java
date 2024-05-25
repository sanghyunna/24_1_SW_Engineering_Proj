//package cau.se.issuemanagespring.get;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.junit.jupiter.api.extension.ExtendWith;
//import static org.hamcrest.Matchers.*;
//
//
//import cau.se.issuemanagespring.controller.IssueController;
//import cau.se.issuemanagespring.service.IssueService;
//import cau.se.issuemanagespring.domain.Issue;
//import cau.se.issuemanagespring.domain.Status;
//import cau.se.issuemanagespring.dto.IssueRequest;
//import cau.se.issuemanagespring.dto.IssueResponse;
//import cau.se.issuemanagespring.domain.Project;
//
//
//
//import java.util.Arrays;
//import java.util.List;
//
//
//@ExtendWith(MockitoExtension.class)
//public class GetAllIssues {
//	
//	
//	private MockMvc mockMvc;
//
//    @Mock
//    private IssueService issueService;
//
//    @InjectMocks
//    private IssueController issueController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
//    }
//    
//    
//    @Test
//    public void testGetAllIssues() throws Exception {
//    	
//    	Project project = new Project();
//    	project.setId(10L);
//    	
//    	Issue issue1 = new Issue();
//    	issue1.setId(1L);
//    	issue1.setTitle("Issue 1");
//    	issue1.setStatus(Status.ASSIGNED);
//    	issue1.setProject(project);
//
//    	
//    	
//    	Issue issue2 = new Issue();
//    	issue2.setId(2L);
//    	issue2.setTitle("Issue 2");
//    	issue2.setStatus(Status.NEW);
//    	issue2.setProject(project);
//    	
//    	Issue issue3 = new Issue();
//    	issue3.setId(3L);
//    	issue3.setTitle("Issue 3");
//    	issue3.setStatus(Status.CLOSED);
//    	issue3.setProject(project);
//    	
//        List<IssueResponse> issues = issueService.getAll(10L);
//
//        mockMvc.perform(get("/project/{projectId}/issue", 10L))
//            .andExpect(status().isOk())                // https status 200
//            .andExpect(jsonPath("$[0].id", is(1)))
//            .andExpect(jsonPath("$[1].id", is(2)))
//            
//            .andExpect(jsonPath("$[0].title", is("Issue 1")))
//            .andExpect(jsonPath("$[1].title", is("Issue 2")))
//            
//            .andExpect(jsonPath("$[1].status", is("NEW")))
//            .andExpect(jsonPath("$[2].status", is("CLOSED")))
//            
//            .andExpect(jsonPath("$", hasSize(3)));
//
//
//        verify(issueService, times(1)).getAll(10L);
//    }
//
//}
