DROP TABLE "comment";
DROP TABLE "task";
DROP TABLE "priority";
DROP TABLE "status";
DROP TABLE "task_list";
DROP TABLE "app_user";
DROP TABLE "notification";


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
    status_value VARCHAR(50) NOT NULL,
    status_order INTEGER     NOT NULL
);

CREATE TABLE "priority"
(
    priority_id    SERIAL PRIMARY KEY,
    priority_level VARCHAR(50) NOT NULL,
    priority_order INTEGER     NOT NULL
);

CREATE TABLE "task"
(
    task_id               SERIAL PRIMARY KEY,
    task_list_id          INT REFERENCES "task_list" (task_list_id),
    task_status_id        INT REFERENCES "status" (status_id),
    task_priority_id      INT REFERENCES "priority" (priority_id),
    task_assigned_user_id INT REFERENCES "app_user" (app_user_id),
    task_title            VARCHAR(100) NOT NULL,
    task_description      TEXT,
    task_due_date         TIMESTAMP,
    task_created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "comment"
(
    comment_id         SERIAL PRIMARY KEY,
    comment_task_id    INT REFERENCES "task" (task_id),
    comment_user_id    INT REFERENCES "app_user" (app_user_id),
    comment_content    TEXT NOT NULL,
    comment_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "notification"
(
    notification_id          SERIAL PRIMARY KEY,
    notification_user_id     INT REFERENCES "app_user" (app_user_id),
    notification_text        VARCHAR(1000) NOT NULL,
    notification_link        VARCHAR(300),
    notification_dismissed   BOOLEAN NOT NULL DEFAULT FALSE,
    notification_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);





INSERT INTO public.priority (priority_id, priority_level, priority_order)
VALUES (DEFAULT, 'Urgent', 1);
INSERT INTO public.priority (priority_id, priority_level, priority_order)
VALUES (DEFAULT, 'High', 2);
INSERT INTO public.priority (priority_id, priority_level, priority_order)
VALUES (DEFAULT, 'Normal', 3);
INSERT INTO public.priority (priority_id, priority_level, priority_order)
VALUES (DEFAULT, 'Low', 4);


INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'New', 1);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'Confirmed', 2);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'Planned', 3);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'In Progress', 4);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'Verification', 5);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'Completed', 6);
INSERT INTO public.status (status_id, status_value, status_order)
VALUES (DEFAULT, 'On Hold', 7);