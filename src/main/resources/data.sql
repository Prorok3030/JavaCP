INSERT IGNORE INTO difficulty (id, name, points) VALUES (1, 'Низкая', 5);
INSERT IGNORE INTO difficulty (id, name, points) VALUES (2, 'Средняя', 10);
INSERT IGNORE INTO difficulty (id, name, points) VALUES (3, 'Высокая', 20);

INSERT IGNORE INTO users (id, active, communication, creativity, experience, health, intelligence, level, password, strength, username)
VALUES (1, true, 0, 0, 0, 0, 0, 1, 'admin123', 0, 'Admin');
INSERT IGNORE INTO user_role (user_id, roles) VALUES (1, 'ADMIN');
INSERT IGNORE INTO category (id, name, user_id) VALUES (1, 'Общее', 1);
