package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.domain.Auth;
import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.repository.AuthRepository;
import cau.se.issuemanagespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    public UserResponse create(UserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getPassword() == null) {
            return null;
        }

        User user = new User();
        user.setName(userRequest.getName());
        userRepository.save(user);

        Auth auth = new Auth();
        auth.setUser(user);
        auth.setPassword(userRequest.getPassword());
        auth.setToken(null);
        authRepository.save(auth); 

        return getUserResponse(user);
    }

    public List<UserResponse> getAll() {
        return getUserResponseList(userRepository.findAllByOrderById());
    }

    public UserResponse update(UserRequest userRequest, String authUser) {
        User user = userRepository.findByName(authUser).orElse(null);
        if (user == null) {
            return null;
        }

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
            userRepository.save(user);
        }
        if (userRequest.getPassword() != null) {
            Auth auth = authRepository.findByUserName(authUser).orElseThrow(() -> new NoSuchElementException("Auth not found"));
            auth.setPassword(userRequest.getPassword());
            authRepository.save(auth);
        }

        return getUserResponse(user);
    }

    public UserResponse getUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public List<UserResponse> getUserResponseList(List<User> users) {
        return users.stream().map(this::getUserResponse).collect(Collectors.toList());
    }
}
