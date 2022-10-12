INSERT IGNORE INTO difficulty (id, name, points) VALUES (1, 'Низкая', 5), (2, 'Средняя', 10), (3, 'Высокая', 20);

INSERT IGNORE INTO skill (id, name) VALUES (1, 'Сила'), (2, 'Интеллект'), (3, 'Здоровье'), (4, 'Креативность'), (5, 'Общение');

INSERT IGNORE INTO users (id, active, communication, creativity, experience, health, intelligence, level, password, strength, username)
VALUES (1, true, 0, 0, 0, 0, 0, 1, 'admin123', 0, 'Admin');
INSERT IGNORE INTO user_role (user_id, roles) VALUES (1, 'ADMIN');
INSERT IGNORE INTO category (id, name, user_id) VALUES (2, 'Общее', 1);
