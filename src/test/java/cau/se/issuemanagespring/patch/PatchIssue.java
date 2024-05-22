package cau.se.issuemanagespring.patch;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.hamcrest.Matchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import cau.se.issuemanagespring.domain.Issue;
import cau.se.issuemanagespring.dto.IssueRequest;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.IssueService;
import cau.se.issuemanagespring.controller.IssueController;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.repository.IssueRepository;
import cau.se.issuemanagespring.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.Arrays;


@ExtendWith(SpringExtension.class)
public class PatchIssue {

    private MockMvc mockMvc;

    @Mock
    private IssueService issueService;

    @Mock
    private AuthService authService;
    
    @Mock
    private User user;
    
    @MockBean
    private IssueRepository issueRepository;
    
    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private IssueController issueController;
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
        
        Issue issue = new Issue();
        issue.setId(1L);  
        issue.setTitle("Updated Title");
        issue.setContent("Updated Content");

        when(issueService.create(any(IssueRequest.class), eq("sam"))).thenReturn(issue);
    }

    @Test
    public void testUpdateIssue() throws Exception {
    	
    	
    	
    	// given
    	User sam = new User();
        sam.setName("sam");
        sam.setId(1L);
        when(userRepository.save(sam)).thenReturn(sam);
        
        
        IssueRequest issueRequest = new IssueRequest(
                "Updated Title",
                LocalDateTime.now().toString(),
                "Updated Content",
                Arrays.asList("sam"),
                "MINOR",
                "VALID_TOKEN"
        );
        
        Issue updatedIssue = issueService.create(issueRequest, "sam");

        when(authService.authenticate(issueRequest.getToken())).thenReturn("sam"); // 인증된 사용자
        when(issueService.update(1L, issueRequest, "sam")).thenReturn(updatedIssue); // 업데이트 결과 Mocking

        // when & then
        mockMvc.perform(patch("/issue/{issueId}", updatedIssue.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(issueRequest)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.id", Matchers.is(updatedIssue.getId())))
                //.andExpect(jsonPath("$.title", Matchers.is(updatedIssue.getTitle())))  // 업데이트된 제목 확인
                //.andExpect(jsonPath("$.content", Matchers.is(updatedIssue.getContent())));  // 업데이트된 내용 확인
    }

//    @Test
//    public void testUpdateIssue_Unauthorized() throws Exception {
//        // given
//        Long issueId = 1L;
//        IssueRequest issueRequest = new IssueRequest();
//        issueRequest.setToken("INVALID_TOKEN");
//
//        when(authService.authenticate(issueRequest.getToken())).thenReturn(null);
//
//        // when & then
//        mockMvc.perform(patch("/issue/{issueId}", issueId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(issueRequest)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void testUpdateIssue_NotFound() throws Exception {
//        // given
//        Long issueId = 1L;
//        IssueRequest issueRequest = new IssueRequest();
//        issueRequest.setToken("VALID_TOKEN");
//
//        when(authService.authenticate(issueRequest.getToken())).thenReturn("authenticatedUser");
//        when(issueService.update(issueId, issueRequest, "authenticatedUser")).thenReturn(null);
//
//        // when & then
//        mockMvc.perform(patch("/issue/{issueId}", issueId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(issueRequest)))
//                .andExpect(status().isNotFound());
//    }
}