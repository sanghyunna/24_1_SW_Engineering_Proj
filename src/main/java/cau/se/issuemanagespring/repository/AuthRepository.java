package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUserId(Long userId);
}
