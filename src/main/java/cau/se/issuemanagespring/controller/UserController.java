package cau.se.issuemanagespring.controller;

import cau.se.issuemanagespring.domain.User;
import cau.se.issuemanagespring.dto.UserRequest;
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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        User createdUser = userService.create(userRequest);
        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        User updatedUser = userService.update(userId, userRequest);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(updatedUser);
    }

}
