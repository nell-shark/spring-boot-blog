DROP EXTENSION IF EXISTS "uuid-ossp" cascade;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS articles cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS comments cascade;

CREATE TABLE articles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    title VARCHAR(255) NOT NULL UNIQUE,
    content TEXT NOT NULL,
    local_date DATE NOT NULL,
    thumbnail TEXT DEFAULT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    avatar TEXT DEFAULT NULL
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    article_id UUID REFERENCES articles(id) NOT NULL,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    content TEXT NOT NULL,
    local_date DATE NOT NULL
);

INSERT INTO articles (id, title, content, local_date, thumbnail) VALUES
    ('40e6215d-b5c6-4896-987c-f30f3678f608',
     'QAnon, adrift after Trump’s defeat, finds new life in Elon Musk’s Twitter',
     '',
     '2022-10-22',
     '/storage/articles/1/thumbnail.png'),

    ('6ecd8c99-4036-403d-bf84-cf8400f67836',
     'For new NCI director, work turns personal: She is diagnosed with cancer',
     '',
     '2022-10-23',
     '/storage/articles/2/thumbnail.png'),

    ('3f333df6-90a4-4fda-8dd3-9485d27cee36',
     'In a new cocktail book, New Orleans has a Cure for what ails you',
     '',
     '2022-10-24',
     '/storage/articles/3/thumbnail.png'),

    ('0e37df36-f698-11e6-8dd4-cb9ced3df976',
     'Kale salad with squash and pomegranate is a feast for the eyes',
     '',
     '2022-10-25',
     '/storage/articles/4/thumbnail.png');

-- password = password123
INSERT INTO users (email, password, role, avatar) VALUES
    ('user1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', '/storage/users/1/avatar.svg'),
    ('user2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', '/storage/users/2/avatar.svg'),
    ('user3@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_USER', NULL),
    ('moderator1@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_MODERATOR', NULL),
    ('moderator2@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_MODERATOR', NULL),
    ('admin@gmail.com', '$2a$12$zfpFafvo1HSyBh.rF6XNPeKpVIdA49iqGdKtV/BBlUlFu8WWT8squ', 'ROLE_ADMIN', NULL);

INSERT INTO comments (article_id, user_id, content, local_date) VALUES
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 1, 'Good news', '2022-10-22'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 2, 'Hello there', '2022-9-22'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 3, 'Hi', '2022-8-22'),
    ('40e6215d-b5c6-4896-987c-f30f3678f608', 4, 'Hello', '2022-7-22'),
    ('6ecd8c99-4036-403d-bf84-cf8400f67836', 1, 'Interesting', '2022-6-22');
