package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
}
