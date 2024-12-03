CREATE TABLE "jee-tasks"."User"
(
    user_id            SERIAL PRIMARY KEY,
    user_email         VARCHAR(100) UNIQUE NOT NULL,
    user_password_hash VARCHAR(200)        NOT NULL,
    user_created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "jee-tasks"."List"
(
    list_id          SERIAL PRIMARY KEY,
    list_user_id     INT REFERENCES "jee-tasks"."User" (user_id),
    list_title       VARCHAR(100) NOT NULL,
    list_description TEXT,
    list_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "jee-tasks"."Status"
(
    status_id    SERIAL PRIMARY KEY,
    status_value VARCHAR(50) NOT NULL
);

CREATE TABLE "jee-tasks"."Priority"
(
    priority_id    SERIAL PRIMARY KEY,
    priority_level VARCHAR(50) NOT NULL
);

CREATE TABLE "jee-tasks"."Task"
(
    task_id          SERIAL PRIMARY KEY,
    task_list_id     INT REFERENCES "jee-tasks"."List" (list_id),
    task_status_id   INT REFERENCES "jee-tasks"."Status" (status_id),
    task_priority_id INT REFERENCES "jee-tasks"."Priority" (priority_id),
    task_title       VARCHAR(100) NOT NULL,
    task_description TEXT,
    task_due_date    TIMESTAMP,
    task_created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "jee-tasks"."Comment"
(
    comment_id         SERIAL PRIMARY KEY,
    comment_task_id    INT REFERENCES "jee-tasks"."Task" (task_id),
    comment_user_id    INT REFERENCES "jee-tasks"."User" (user_id),
    comment_content    TEXT NOT NULL,
    comment_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);