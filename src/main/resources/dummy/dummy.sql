-- user table data
insert into users (name, username, email, password, role)
values ('Alice', 'alice', 'alice@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'ADMIN'),
       ('Bob', 'bob', 'bob@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Charlie', 'charlie', 'charlie@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('David', 'david', 'david@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Eve', 'eve', 'eve@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Frank', 'frank', 'frank@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Grace', 'grace', 'grace@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Heidi', 'heidi', 'heidi@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Ivan', 'ivan', 'ivan@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Judy', 'judy', 'judy@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Mallory', 'mallory', 'mallory@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Niaj', 'niaj', 'niaj@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Olivia', 'olivia', 'olivia@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Peggy', 'peggy', 'peggy@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Quinn', 'quinn', 'quinn@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Ruth', 'ruth', 'ruth@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER'),
       ('Sybil', 'sybil', 'sybil@example.com',
        '{bcrypt}$2a$10$9z/zl1s7uQuffBlrww1.Du4nNKJSn173CbnfNCXLSBT3YG13P3RnS', 'USER');

-- team table data
insert into team (creation_waiting, deletion_waiting, name, team_type)
values (0, 0, 'Dev Course Team 1', 'PROJECT'),
       (0, 0, 'Dev Course Team 2', 'PROJECT'),
       (0, 0, 'Dev Course Team 3', 'PROJECT'),
       (0, 0, 'Dev Course Team 4', 'PROJECT');


-- accessType table data
insert into accessType (id, is_owner, team_id, user_id, course)
values (1, 0, null, null, '데브코스 1기'),                   -- 코스 기본 레코드 (관리자용)
       (2, 1, (select id from team where name = 'Dev Course Team 1'),
        (select id from users where username = 'bob'), '데브코스 1기'),     -- 팀장 레코드 (데브코스 1기)
       (3, 0, (select id from team where name = 'Dev Course Team 1'),
        (select id from users where username = 'charlie'), '데브코스 1기'), -- 팀원 레코드 (데브코스 1기)
       (4, 0, (select id from team where name = 'Dev Course Team 1'),
        (select id from users where username = 'david'), '데브코스 1기'),   -- 팀원 레코드 (데브코스 1기)
       (5, 0, null, null, '데브코스 2기'),                   -- 코스 기본 레코드 (관리자용)
       (6, 1, (select id from team where name = 'Dev Course Team 2'),
        (select id from users where username = 'frank'), '데브코스 2기'),   -- 팀장 레코드 (데브코스 2기)
       (7, 0, (select id from team where name = 'Dev Course Team 2'),
        (select id from users where username = 'grace'), '데브코스 2기'),   -- 팀원 레코드 (데브코스 2기)
       (8, 0, (select id from team where name = 'Dev Course Team 2'),
        (select id from users where username = 'heidi'), '데브코스 2기'),   -- 팀원 레코드 (데브코스 2기)
       (9, 0, null, null, '데브코스 3기'),                   -- 코스 기본 레코드 (관리자용)
       (10, 1, (select id from team where name = 'Dev Course Team 3'),
        (select id from users where username = 'judy'), '데브코스 3기'),    -- 팀장 레코드 (데브코스 3기)
       (11, 0, (select id from team where name = 'Dev Course Team 3'),
        (select id from users where username = 'mallory'), '데브코스 3기'), -- 팀원 레코드 (데브코스 3기)
       (12, 0, (select id from team where name = 'Dev Course Team 3'),
        (select id from users where username = 'niaj'), '데브코스 3기'),    -- 팀원 레코드 (데브코스 3기)
       (13, 0, null, null, '데브코스 4기'),                  -- 코스 기본 레코드 (관리자용)
       (14, 1, (select id from team where name = 'Dev Course Team 4'),
        (select id from users where username = 'olivia'), '데브코스 4기'),  -- 팀장 레코드 (데브코스 4기)
       (15, 0, (select id from team where name = 'Dev Course Team 4'),
        (select id from users where username = 'peggy'), '데브코스 4기'),   -- 팀원 레코드 (데브코스 4기)
       (16, 0, (select id from team where name = 'Dev Course Team 4'),
        (select id from users where username = 'quinn'), '데브코스 4기');   -- 팀원 레코드 (데브코스 4기)


-- attendance table data
insert into attendance (creation_waiting, end_at, start_at, user_id, description, type)
values (0, '2024-10-04 17:00:00', '2024-10-04 09:00:00',
        (select id from users where username = 'charlie'), 'Late due to traffic', 'LATE'),
       (0, '2024-10-04 18:00:00', '2024-10-04 10:00:00',
        (select id from users where username = 'bob'), 'Outing for a meeting', 'OUTING');

-- calendar table data
insert into calendar (belonging_id)
values ((select id from accessType where course = '데브코스 1기' and is_owner = 0 and team_id IS NULL)), -- 데브코스 1기 공통 캘린더
       ((select id from accessType where course = '데브코스 1기' and is_owner = 1)),                     -- 데브코스 1기 팀 캘린더
       ((select id from accessType where course = '데브코스 2기' and is_owner = 0 and team_id IS NULL)), -- 데브코스 2기 공통 캘린더
       ((select id from accessType where course = '데브코스 2기' and is_owner = 1)),                     -- 데브코스 2기 팀 캘린더
       ((select id from accessType where course = '데브코스 3기' and is_owner = 0 and team_id IS NULL)), -- 데브코스 3기 공통 캘린더
       ((select id from accessType where course = '데브코스 3기' and is_owner = 1)),                     -- 데브코스 3기 팀 캘린더
       ((select id from accessType where course = '데브코스 4기' and is_owner = 0 and team_id IS NULL)), -- 데브코스 4기 공통 캘린더
       ((select id from accessType where course = '데브코스 4기' and is_owner = 1)); -- 데브코스 4기 팀 캘린더

-- schedule table data
insert into schedule (calendar_id, end_at, start_at, name, description, schedule_type)
values ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 1기' and is_owner = 0 and team_id IS NULL)),
        '2024-10-05 15:00:00', '2024-10-05 14:00:00', 'Common Meeting',
        'General meeting for all courses', 'MEETING'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 1기' and is_owner = 1)),
        '2024-10-06 12:00:00', '2024-10-06 10:00:00', 'Team Scrum', 'Daily scrum for the team',
        'SCRUM'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 2기' and is_owner = 0 and team_id IS NULL)),
        '2024-10-07 15:00:00', '2024-10-07 14:00:00', 'Dev Course 2 Meeting', 'Meeting for 데브코스 2기',
        'MEETING'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 2기' and is_owner = 1)),
        '2024-10-08 15:00:00', '2024-10-08 14:00:00', 'Dev Course 2 Team Scrum',
        'Daily scrum for 데브코스 2기 팀', 'SCRUM'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 3기' and is_owner = 0 and team_id IS NULL)),
        '2024-10-08 15:00:00', '2024-10-08 14:00:00', 'Dev Course 3 Meeting', 'Meeting for 데브코스 3기',
        'MEETING'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 3기' and is_owner = 1)),
        '2024-10-09 15:00:00', '2024-10-09 14:00:00', 'Dev Course 3 Team Scrum',
        'Daily scrum for 데브코스 3기 팀', 'SCRUM'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 4기' and is_owner = 0 and team_id IS NULL)),
        '2024-10-09 15:00:00', '2024-10-09 14:00:00', 'Dev Course 4 Meeting', 'Meeting for 데브코스 4기',
        'MEETING'),
       ((select id
         from calendar
         where belonging_id = (select id from accessType where course = '데브코스 4기' and is_owner = 1)),
        '2024-10-10 15:00:00', '2024-10-10 14:00:00', 'Dev Course 4 Team Scrum',
        'Daily scrum for 데브코스 4기 팀', 'SCRUM');