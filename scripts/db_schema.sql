DROP TABLE "comment";
DROP TABLE "task";
DROP TABLE "priority";
DROP TABLE "status";
DROP TABLE "task_list";
DROP TABLE "app_user";


CREATE TABLE "app_user"
(
    app_user_id            SERIAL PRIMARY KEY,
    app_user_email         VARCHAR(100) UNIQUE NOT NULL,
    app_user_password_hash VARCHAR(200)        NOT NULL,
    app_user_created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "task_list"
(
    task_list_id          SERIAL PRIMARY KEY,
    task_list_user_id     INT REFERENCES "app_user" (app_user_id),
    task_list_title       VARCHAR(100) NOT NULL,
    task_list_description TEXT,
    task_list_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "status"
(
    status_id    SERIAL PRIMARY KEY,
    status_value VARCHAR(50) NOT NULL
);

CREATE TABLE "priority"
(
    priority_id    SERIAL PRIMARY KEY,
    priority_level VARCHAR(50) NOT NULL
);

CREATE TABLE "task"
(
    task_id          SERIAL PRIMARY KEY,
    task_list_id     INT REFERENCES "task_list" (task_list_id),
    task_status_id   INT REFERENCES "status" (status_id),
    task_priority_id INT REFERENCES "priority" (priority_id),
    task_title       VARCHAR(100) NOT NULL,
    task_description TEXT,
    task_due_date    TIMESTAMP,
    task_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "comment"
(
    comment_id         SERIAL PRIMARY KEY,
    comment_task_id    INT REFERENCES "task" (task_id),
    comment_user_id    INT REFERENCES "app_user" (app_user_id),
    comment_content    TEXT NOT NULL,
    comment_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



INSERT INTO public.priority (priority_id, priority_level) VALUES (DEFAULT, 'Urgent');
INSERT INTO public.priority (priority_id, priority_level) VALUES (DEFAULT, 'High');
INSERT INTO public.priority (priority_id, priority_level) VALUES (DEFAULT, 'Normal');
INSERT INTO public.priority (priority_id, priority_level) VALUES (DEFAULT, 'Low');


INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'New');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'Confirmed');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'Planned');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'In Progress');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'Verification');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'Completed');
INSERT INTO public.status (status_id, status_value) VALUES (DEFAULT, 'On Hold');