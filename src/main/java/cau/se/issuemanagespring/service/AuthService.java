package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.TokenResponse;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    public String authenticate(String token) {
        if (token == null) {
            return null;
        }

        Auth auth = authRepository.findByToken(token).orElse(null);
        if (auth == null) {
            return null;
        }

        return auth.getUser().getName();
    }

    public TokenResponse login(UserRequest userRequest) {
        User user = userRepository.findByName(userRequest.getName()).orElse(null);
        if (user == null) {
            return null;
        }

        Auth auth = authRepository.findByUserId(user.getId()).orElse(null);
        if (auth == null || !auth.getPassword().equals(userRequest.getPassword())) {
            return null;
        }

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setName(userRequest.getName());

        int random = (int) (Math.random() * 90000) + 10000;
        String token = user.getName() + ":" + auth.getPassword() + ":" + random;
        tokenResponse.setToken(token);

        // Auth에 토큰 저장
        auth.setToken(token);
        authRepository.save(auth);

        return tokenResponse;
    }

    public boolean logout(String authUser) {
        User user = userRepository.findByName(authUser).orElse(null);
        if (user == null) {
            return false;
        }

        Auth auth = authRepository.findByUserId(user.getId()).orElse(null);
        if (auth == null) {
            return false;
        }

        auth.setToken(null);
        return true;
    }
}
