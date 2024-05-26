package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderById();
    Optional<User> findByName(String name);
}
