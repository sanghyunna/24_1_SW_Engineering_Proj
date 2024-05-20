package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.TokenRequest;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest) {
        TokenResponse tokenResponse = authService.login(userRequest);
        if (tokenResponse == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRequest tokenRequest) {
        String authUser = authService.authenticate(tokenRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(401).build();
        }

        boolean logout = authService.logout(authUser);
        if (!logout) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok().build();
    }
}
