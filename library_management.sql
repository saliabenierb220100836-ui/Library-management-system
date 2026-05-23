-- ============================================================
--  Library Management System – Database Schema
--  Run this script in your PostgreSQL database to set up
--  the required tables and seed data.
-- ============================================================

-- 1. User accounts (for login / sign-up)
CREATE TABLE IF NOT EXISTS user_accounts (
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL          -- SHA-256 hex
);

-- 2. Genres (lookup table for the ComboBox)
CREATE TABLE IF NOT EXISTS genres (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Books
CREATE TABLE IF NOT EXISTS books (
    id               SERIAL PRIMARY KEY,
    isbn             VARCHAR(20)  NOT NULL UNIQUE,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255) NOT NULL,
    genre            VARCHAR(100) NOT NULL,
    published_year   INT          NOT NULL,
    total_copies     INT          NOT NULL DEFAULT 1,
    available_copies INT          NOT NULL DEFAULT 1
);

-- ────────────────────────────────────────────
--  Seed: genres
-- ────────────────────────────────────────────
INSERT INTO genres (name) VALUES
    ('Fiction'),
    ('Non-Fiction'),
    ('Science Fiction'),
    ('Fantasy'),
    ('Mystery'),
    ('Biography'),
    ('History'),
    ('Technology'),
    ('Self-Help'),
    ('Children')
ON CONFLICT (name) DO NOTHING;

-- ────────────────────────────────────────────
--  Seed: sample books
-- ────────────────────────────────────────────
INSERT INTO books (isbn, title, author, genre, published_year, total_copies, available_copies) VALUES
    ('978-0-06-112008-4', 'To Kill a Mockingbird', 'Harper Lee',        'Fiction',     1960, 3, 3),
    ('978-0-7432-7356-5', '1984',                  'George Orwell',     'Fiction',     1949, 2, 2),
    ('978-0-618-00221-3', 'The Fellowship of the Ring', 'J.R.R. Tolkien','Fantasy',    1954, 2, 1),
    ('978-0-14-028329-7', 'Dune',                  'Frank Herbert',     'Science Fiction', 1965, 2, 2),
    ('978-0-7432-7352-7', 'The Da Vinci Code',     'Dan Brown',         'Mystery',     2003, 4, 3)
ON CONFLICT (isbn) DO NOTHING;

-- ────────────────────────────────────────────
--  Seed: default admin account
--  Username: admin
--  Password: admin123
--  (SHA-256 of "admin123")
-- ────────────────────────────────────────────
INSERT INTO user_accounts (username, password_hash) VALUES
    ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9')
ON CONFLICT (username) DO NOTHING;
