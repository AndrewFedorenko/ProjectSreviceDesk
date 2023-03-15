insert into public.projects (name) values ('projxxx');

insert into public.role (name,is_executor, is_admin) values
                                                         ('admin', false, true),
                                                         ('client',false, false),
                                                         ('executor',true, false);

insert into public.users (email, is_enabled, name, password, role_id) values ('itprofit@ukr.net', true, 'admin', '$2y$10$BtXTJYsQgcjSlJVfx1a/J.grZ7VV95nSKBR9l0FJayZ/Bh7mHOzbm', 1);
insert into public.users (email, is_enabled, name, password, role_id) values ('itprofit@i.ua', true, 'author', '$2y$10$MWxj8Zm5sZAZt9ejI1Fc4epCrgA84QWVZsr/skjLoMP06747KwY.W',2);
insert into public.users (email, is_enabled, name, password, role_id) values ('fast_fae@ukr.net', true, 'user', '$2y$10$BT4lJdxzEDksTyI.5oD2w.wq4wG6iTgUEM9RqLSnF8BJE2ZeMPkle',3);
insert into public.users (email, is_enabled, name, password, role_id) values ('itprofitua@gmail.com', true, 'user2', '$2y$10$BT4lJdxzEDksTyI.5oD2w.wq4wG6iTgUEM9RqLSnF8BJE2ZeMPkle',3);

insert into public.filters (requests_filter, projects_filter, users_filter, id)
    values ('where (sr.user.id=:userid or sr.user=null) and sr.project in (select u.projects from User u where u.id=:userid)',
               'inner join p.users u where u.id=:userid',
            ' inner join us.projects p1 where p1 in (select p from Project p inner join p.users u where u.id=:userid) and us.isEnabled ', 3);

insert into public.filters (requests_filter, projects_filter, users_filter, id)
values ('where (sr.author.id=:userid) and sr.project in (select u.projects from User u where u.id=:userid)',
        'inner join p.users u where u.id=:userid',
        ' inner join us.projects p1 where p1 in (select p from Project p inner join p.users u where u.id=:userid) and us.isEnabled ', 2);

insert into public.statuses (name) values ('in work');
insert into public.statuses (name) values ('done');
insert into public.customers (enabled, name) values (true, 'customer');

insert into public.request_read_only_field (field,is_forbidden_to_edit,is_forbidden_to_filter) values
                                                       ('date'      ,true, true), --1
                                                       ('id'        ,true, true), --2
                                                       ('author'    ,true, false), --3
                                                       ('author'    ,true, true), --4
                                                       ('customer'  ,true, false), --5
                                                       ('user'      ,true, true), --6
                                                       ('user'      ,true, false), --7
                                                       ('content'   ,true, true), --8
                                                       ('answer'    ,true, true), --9
                                                       ('project'   ,true, false); --10

-- for request authors
insert into public.role_request_read_only_fields (role_id, request_read_only_fields_id) values
                                                                      ('2','1'),
                                                                      ('2','2'),
                                                                      ('2','4'),
                                                                      ('2','7'),
                                                                      ('2','9');
-- for request executors
insert into public.role_request_read_only_fields (role_id, request_read_only_fields_id) values
                                                                      ('3','1'),
                                                                      ('3','2'),
                                                                      ('3','3'),
                                                                      ('3','5'),
                                                                      ('3','6'),
                                                                      ('3','8'),
                                                                      ('3','10');



insert into public.requests (content, date, author_id, customer_id, status_id, user_id, project_id,answer)
values
    ('help initial','2023-01-01',1,1,1,1,1,'');

insert into public.requests (content, date, author_id, customer_id, status_id, user_id, project_id,answer)
values
    ('help help','2023-02-01',1,1,1,1,1,'');

insert into public.requests (content, date, author_id, customer_id, status_id, user_id, project_id,answer)
values
    ('help help','2023-02-01',1,1,2,3,1,'');

insert into public.requests (content, date, author_id, customer_id, status_id, user_id, project_id)
values
    ('help help','2023-02-01',2,1,2,null,1);

insert into public.user_project (user_id, project_id) values (2,1), (3,1), (4,1);

insert into public.email_profile (address, is_active,login,password,port,smtp_host)
        values ('fortechsend@ukr.net',true,'fortechsend@ukr.net','v3hEMrIVrkVWDVpx',465, 'smtp.ukr.net');


insert into public.required_field (entity_name,field_name) values ('SupportRequest','author'),
                                                                  ('SupportRequest','project'),
                                                                  ('SupportRequest','status'),
                                                                  ('SupportRequest','customer');
insert into public.required_field (entity_name,field_name) values ('User','name'),
                                                                  ('User','email');
insert into public.required_field (entity_name,field_name) values ('EmailProfile','address'),
                                                                  ('EmailProfile','smtpHost'),
                                                                  ('EmailProfile','login'),
                                                                  ('EmailProfile','password'),
                                                                  ('EmailProfile','port');
