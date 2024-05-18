package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}
