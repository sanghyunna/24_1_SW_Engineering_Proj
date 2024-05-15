package cau.se.issuemanagespring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class defaultController {
    @GetMapping("/")
    public ResponseEntity<Map<String,String>> defaultResponse() {
        Map<String,String> response = Map.of(
                "status", "200",
                "message", "This is the root directory"
        );
        return ResponseEntity.ok(response);
    }
}
