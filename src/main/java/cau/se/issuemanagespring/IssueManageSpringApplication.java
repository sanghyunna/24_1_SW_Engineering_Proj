package cau.se.issuemanagespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IssueManageSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueManageSpringApplication.class, args);
    }

}
