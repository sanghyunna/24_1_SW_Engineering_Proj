-- User 테이블 생성
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

-- Auth 테이블 생성
CREATE TABLE auth (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      token VARCHAR(255),
                      FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Project 테이블 생성
CREATE TABLE project (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         create_date TIMESTAMP,
                         update_date TIMESTAMP,
                         project_owner_id BIGINT,
                         FOREIGN KEY (project_owner_id) REFERENCES user(id)
);

-- Project_PLs 테이블 생성 (Project와 PL 사용자 간의 중간 테이블)
CREATE TABLE project_pls (
                             project_id BIGINT NOT NULL,
                             pls_id BIGINT NOT NULL,
                             PRIMARY KEY (project_id, pls_id),
                             FOREIGN KEY (project_id) REFERENCES project(id),
                             FOREIGN KEY (pls_id) REFERENCES user(id)
);

-- Project_Devs 테이블 생성 (Project와 Dev 사용자 간의 중간 테이블)
CREATE TABLE project_devs (
                              project_id BIGINT NOT NULL,
                              devs_id BIGINT NOT NULL,
                              PRIMARY KEY (project_id, devs_id),
                              FOREIGN KEY (project_id) REFERENCES project(id),
                              FOREIGN KEY (devs_id) REFERENCES user(id)
);

-- Project_Testers 테이블 생성 (Project와 Tester 사용자 간의 중간 테이블)
CREATE TABLE project_testers (
                                 project_id BIGINT NOT NULL,
                                 testers_id BIGINT NOT NULL,
                                 PRIMARY KEY (project_id, testers_id),
                                 FOREIGN KEY (project_id) REFERENCES project(id),
                                 FOREIGN KEY (testers_id) REFERENCES user(id)
);

-- Issue 테이블 생성
CREATE TABLE issue (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       create_date TIMESTAMP,
                       update_date TIMESTAMP,
                       due_date TIMESTAMP,
                       content TEXT,
                       project_id BIGINT,
                       reporter_id BIGINT,
                       fixer_id BIGINT,
                       status VARCHAR(255),
                       priority VARCHAR(255),
                       FOREIGN KEY (project_id) REFERENCES project(id),
                       FOREIGN KEY (reporter_id) REFERENCES user(id),
                       FOREIGN KEY (fixer_id) REFERENCES user(id)
);

-- Issue_Assignee 테이블 생성 (Issue와 Assignee 사용자 간의 중간 테이블)
CREATE TABLE issue_assignee (
                                issue_id BIGINT NOT NULL,
                                assignee_id BIGINT NOT NULL,
                                PRIMARY KEY (issue_id, assignee_id),
                                FOREIGN KEY (issue_id) REFERENCES issue(id),
                                FOREIGN KEY (assignee_id) REFERENCES user(id)
);

-- Comment 테이블 생성
CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         content TEXT,
                         create_date TIMESTAMP,
                         update_date TIMESTAMP,
                         issue_id BIGINT,
                         comment_owner_id BIGINT,
                         FOREIGN KEY (issue_id) REFERENCES issue(id),
                         FOREIGN KEY (comment_owner_id) REFERENCES user(id)
);