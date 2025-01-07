INSERT INTO profiles (id, name, description)
VALUES (gen_random_uuid(), 'ADMIN', 'ADMIN profile - do not delete');

INSERT INTO users (id, name, username, phone, email, birth_date, secret, profile_id)
VALUES (gen_random_uuid(), 'admin', 'admin', '123456', 'admin@useradmin.com', '1987-02-10',
        '$2a$10$0kOJ1xtg6dBoyG3od/66kuflyHU8uDKomtydvcF0e9h3E7KtwE6NK',
        (SELECT id FROM profiles where name like 'ADMIN'));