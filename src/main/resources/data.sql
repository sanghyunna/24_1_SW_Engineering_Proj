-- -- User 엔티티 데이터 추가
-- INSERT INTO user (name) VALUES ('john');
-- INSERT INTO user (name) VALUES ('jane');
-- INSERT INTO user (name) VALUES ('bob');
--
-- INSERT INTO auth (user_id, password) VALUES (1, 'password1');
-- INSERT INTO auth (user_id, password) VALUES (2, 'password2');
-- INSERT INTO auth (user_id, password) VALUES (3, 'password3');

-- data.sql

-- Users
INSERT INTO user (name) VALUES ('john');
INSERT INTO user (name) VALUES ('jane');
INSERT INTO user (name) VALUES ('bob');
INSERT INTO user (name) VALUES ('alice');
INSERT INTO user (name) VALUES ('tom');

INSERT INTO auth (user_id, password, token) VALUES (1, 'password1', '');
INSERT INTO auth (user_id, password, token) VALUES (2, 'password2', '');
INSERT INTO auth (user_id, password, token) VALUES (3, 'password3', '');
INSERT INTO auth (user_id, password, token) VALUES (4, 'password4', '');
INSERT INTO auth (user_id, password, token) VALUES (5, 'password5', '');

-- Projects
INSERT INTO project (title, project_owner_id) VALUES ('Project A', 1);
INSERT INTO project (title, project_owner_id) VALUES ('Project B', 2);

-- Add PLs, Devs, and Testers to projects
INSERT INTO project_pls (project_id, pls_id) VALUES (1, 3);
INSERT INTO project_devs (project_id, devs_id) VALUES (1, 4);
INSERT INTO project_testers (project_id, testers_id) VALUES (1, 5);

INSERT INTO project_pls (project_id, pls_id) VALUES (2, 3);
INSERT INTO project_devs (project_id, devs_id) VALUES (2, 4), (2, 5);

-- Issues'
INSERT INTO issue (title, due_date, content, reporter_id, status, priority) VALUES ('Issue 1', '2023-05-25T12:00:00', 'This is issue 1', 1, 'NEW', 'MAJOR');
INSERT INTO issue (title, due_date, content, reporter_id, status, priority) VALUES ('Issue 2', '2023-06-01T13:00:00', 'This is issue 2', 2, 'ASSIGNED', 'CRITICAL');

-- Add assignees to issues
INSERT INTO issue_assignee (issue_id, assignee_id) VALUES (1, 4);
INSERT INTO issue_assignee (issue_id, assignee_id) VALUES (2, 4), (2, 5);

-- Add fixer to issues
UPDATE issue SET fixer_id = 4 WHERE id = 1;
UPDATE issue SET fixer_id = 5 WHERE id = 2;

-- Comments
INSERT INTO comment (content, issue_id, comment_owner_id) VALUES ('This is a comment for issue 1', 1, 3);
INSERT INTO comment (content, issue_id, comment_owner_id) VALUES ('This is another comment for issue 1', 1, 4);
INSERT INTO comment (content, issue_id, comment_owner_id) VALUES ('This is a comment for issue 2', 2, 5);