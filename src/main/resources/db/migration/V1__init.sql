create table courses (
    id varchar(36) primary key,
    title varchar(200) not null,
    description varchar(2000) not null
);

create table students (
    id varchar(36) primary key,
    full_name varchar(200) not null,
    email varchar(320) not null unique,
    has_access boolean not null default false
);

create table enrollments (
    id varchar(36) primary key,
    student_id varchar(36) not null references students(id) on delete cascade,
    course_id varchar(36) not null references courses(id) on delete cascade,
    enrolled_at timestamp not null,
    constraint uq_enrollment unique (student_id, course_id)
);
