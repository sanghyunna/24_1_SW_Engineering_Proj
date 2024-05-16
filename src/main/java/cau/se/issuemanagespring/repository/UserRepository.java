package cau.se.issuemanagespring.repository;

import cau.se.issuemanagespring.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
