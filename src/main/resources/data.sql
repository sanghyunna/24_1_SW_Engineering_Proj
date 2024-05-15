-- User 엔티티 데이터 추가
INSERT INTO user (name, password) VALUES ('John Doe', '1234');
INSERT INTO user (name, password) VALUES ('Jane Smith', '1234');
INSERT INTO user (name, password) VALUES ('Bob Johnson', '1234');
INSERT INTO user (name, password) VALUES ('Alice Williams', '1234');
INSERT INTO user (name, password) VALUES ('Tom Wilson', '1234');
INSERT INTO user (name, password) VALUES ('Emma Davis', '1234');
INSERT INTO user (name, password) VALUES ('Michael Brown', '1234');

-- Project 엔티티 데이터 추가
INSERT INTO project (title, project_owner_id) VALUES ('Project Alpha', 1);
INSERT INTO project (title, project_owner_id) VALUES ('Project Bravo', 2);

-- Project와 PL, Dev, Tester 관계 설정 (중간 테이블 데이터 추가)
INSERT INTO project_pls (project_id, pls_id) VALUES (1, 2), (1, 5);
INSERT INTO project_devs (project_id, devs_id) VALUES (1, 3), (1, 6);
INSERT INTO project_testers (project_id, testers_id) VALUES (1, 4), (1, 7);

INSERT INTO project_pls (project_id, pls_id) VALUES (2, 2), (2, 5);
INSERT INTO project_devs (project_id, devs_id) VALUES (2, 3), (2, 6);
INSERT INTO project_testers (project_id, testers_id) VALUES (2, 4), (2, 7);