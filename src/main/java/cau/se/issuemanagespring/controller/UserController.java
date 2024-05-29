package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.dto.UserRequest;
import cau.se.issuemanagespring.dto.UserResponse;
import cau.se.issuemanagespring.service.AuthService;
import cau.se.issuemanagespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * User를 생성합니다. 생성된 User를 반환합니다.
     * @param userRequest name, password, token
     * @return UserResponse
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.create(userRequest);
        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * 모든 User를 반환합니다.
     * @return UserResponse의 List
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAll();
        return ResponseEntity.ok().body(users);
    }

    /**
     * userId에 해당하는 User를 수정합니다. 수정된 User를 반환합니다.
     * @param userRequest name, password, token
     * @return UserResponse
     */
    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        // token 검증
        String authUser = authService.authenticate(userRequest.getToken());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse updatedUser = userService.update(userRequest, authUser);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(updatedUser);
    }

}
