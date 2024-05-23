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

    /**
     * User의 이름과 비밀번호를 받고 로그인 token을 반환합니다.
     * @param userRequest name, password
     * @return TokenResponse
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest) {
        TokenResponse tokenResponse = authService.login(userRequest);
        if (tokenResponse == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().body(tokenResponse);
    }

    /**
     * tokend을 받고 해당 token의 효력을 없앱니다.
     * @param tokenRequest token
     * @return 없음
     */
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
