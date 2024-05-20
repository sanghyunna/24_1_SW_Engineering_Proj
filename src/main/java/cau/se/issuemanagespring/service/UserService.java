package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    public User create(UserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getPassword() == null) {
            return null;
        }

        User user = new User();
        user.setName(userRequest.getName());
        userRepository.save(user);

        Auth auth = new Auth();
        auth.setUser(user);
        auth.setPassword(userRequest.getPassword());
        authRepository.save(auth);

        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User update(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
            userRepository.save(user);
        }
        if (userRequest.getPassword() != null) {
            Auth auth = authRepository.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("Auth not found"));
            auth.setPassword(userRequest.getPassword());
            authRepository.save(auth);
        }

        return user;
    }
}
