CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    voter_id VARCHAR(50) UNIQUE,
    is_voter_id_valid BOOLEAN DEFAULT FALSE,
    public_key TEXT,
    private_key TEXT
);

CREATE TABLE elections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    created_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE candidates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    election_id BIGINT,
    FOREIGN KEY (election_id) REFERENCES elections(id)
);

CREATE TABLE blocks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    previous_hash VARCHAR(64),
    timestamp DATETIME NOT NULL,
    voter_id VARCHAR(50),
    election_id BIGINT,
    candidate_id BIGINT,
    nonce BIGINT,
    hash VARCHAR(64),
    signature TEXT,
    FOREIGN KEY (election_id) REFERENCES elections(id),
    FOREIGN KEY (candidate_id) REFERENCES candidates(id)
);