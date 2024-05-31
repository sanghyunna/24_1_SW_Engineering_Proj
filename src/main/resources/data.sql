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
INSERT INTO user (name) VALUES ('admin');

INSERT INTO auth (user_id, password, token) VALUES (1, 'password1', null);
INSERT INTO auth (user_id, password, token) VALUES (2, 'password2', null);
INSERT INTO auth (user_id, password, token) VALUES (3, 'password3', null);
INSERT INTO auth (user_id, password, token) VALUES (4, 'password4', null);
INSERT INTO auth (user_id, password, token) VALUES (5, 'password5', null);
INSERT INTO auth (user_id, password, token) VALUES (6, 'admin', null);

-- Projects
INSERT INTO project (title, create_date, update_date, project_owner_id) VALUES ('Project Alpha', '2023-05-18T10:00:00', '2023-05-18T10:00:00', 1);
INSERT INTO project (title, create_date, update_date, project_owner_id) VALUES ('Project Beta', '2023-05-18T11:00:00', '2023-05-18T11:00:00', 2);

-- Add PLs, Devs, and Testers to projects
INSERT INTO project_pls (project_id, pls_id) VALUES (1, 3);
INSERT INTO project_devs (project_id, devs_id) VALUES (1, 4);
INSERT INTO project_testers (project_id, testers_id) VALUES (1, 5);

INSERT INTO project_pls (project_id, pls_id) VALUES (2, 3);
INSERT INTO project_devs (project_id, devs_id) VALUES (2, 4), (2, 5);

-- Issues'
INSERT INTO issue (title, create_date, update_date, due_date, content, project_id, reporter_id, status, priority) VALUES ('Slow loading times for product pages', '2024-05-18T12:00:00', '2024-05-18T12:00:00', '2024-05-25T12:00:00', 'Users are reporting very slow load times for our product detail pages, especially on mobile devices. We need to optimize the code and assets to improve performance.', 1, 1, 'FIXED', 'MAJOR');
INSERT INTO issue (title, create_date, update_date, due_date, content, project_id, reporter_id, status, priority) VALUES ('Implement shopping cart functionality', '2024-05-25T13:00:00', '2024-05-25T13:00:00', '2024-06-01T13:00:00', 'Currently, users have to go through the checkout process for each item they want to purchase. We need to add a shopping cart so customers can add multiple items before checking out.', 1, 2, 'FIXED', 'CRITICAL');
INSERT INTO issue (title, create_date, update_date, due_date, content, project_id, reporter_id, status, priority) VALUES ('Login page is very slow', '2024-05-30T13:00:00', '2024-05-30T13:00:00', '2024-06-05T13:00:00', 'Users are reporting very slow loading experiences in login page. I think we need to optimize the code to improve performance as soon as possible.', 1, 1, 'NEW', 'CRITICAL');
INSERT INTO issue (title, create_date, update_date, due_date, content, project_id, reporter_id, status, priority) VALUES ('Improve search functionality', '2023-05-18T14:00:00', '2023-05-18T14:00:00', '2023-06-01T14:00:00', 'Users have complained that the current site search is not returning relevant results. We need to re-evaluate the search algorithms and relevancy scoring.', 2, 2, 'NEW', 'CRITICAL');

-- Add assignees to issues
INSERT INTO issue_assignee (issue_id, assignee_id) VALUES (1, 4);
INSERT INTO issue_assignee (issue_id, assignee_id) VALUES (2, 4), (2, 5);

-- Add fixer to issues
UPDATE issue SET fixer_id = 4 WHERE id = 1;
UPDATE issue SET fixer_id = 5 WHERE id = 2;

-- Comments
INSERT INTO comment (create_date, update_date, content, issue_id, comment_owner_id) VALUES ('2024-05-18T12:00:00', '2024-05-18T12:00:00', 'Let''s try implementing code splitting and lazy loading non-critical assets to reduce initial load times.', 1, 3);
INSERT INTO comment (create_date, update_date, content, issue_id, comment_owner_id) VALUES ('2024-05-18T13:00:00', '2024-05-18T13:00:00', 'Compressing images and minifying JS/CSS could also help reduce load times.', 1, 4);
INSERT INTO comment (create_date, update_date, content, issue_id, comment_owner_id) VALUES ('2024-05-18T14:00:00', '2024-05-18T14:00:00', 'Don''t forget to configure browser caching headers correctly.', 1, 5);
INSERT INTO comment (create_date, update_date, content, issue_id, comment_owner_id) VALUES ('2024-05-25T13:00:00', '2024-05-25T13:00:00', 'The cart functionality will require updates to the checkout flow.', 2, 5);
INSERT INTO comment (create_date, update_date, content, issue_id, comment_owner_id) VALUES ('2024-05-25T15:00:00', '2024-05-25T15:00:00', 'We need to decide on storing cart data client-side or server-side.', 2, 2);