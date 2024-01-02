INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$CBfXWsWXAVbH3BLFOiZNpuOTGzGdkR.0YnUaHdW2cu93fKkZ9FW3y', 'admin@test.nl', TRUE);
INSERT INTO authorities(username,authority) VALUES ('admin','ROLE_ADMIN');
