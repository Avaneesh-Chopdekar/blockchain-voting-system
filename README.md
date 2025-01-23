# Blockchain Voting System

## Project Overview

A secure, blockchain-based electronic voting application with cryptographic authentication and vote verification.

## Prerequisites

- Java 11+
- MySQL 8.0+
- Maven/Gradle for dependency management

## Technologies Used

- Java
- MySQL
- RSA Cryptography
- Blockchain Principles

## Database Setup

### Create Database

```sql
CREATE DATABASE voting_system;
USE voting_system;
```

### Create Tables

```sql
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
```

## Folder Structure

```
blockchain-voting/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/voting/system/
│   │   │       ├── models/
│   │   │       ├── services/
│   │   │       ├── utils/
│   │   │       └── VotingApplication.java
│   │   │
│   │   └── resources/
│   │       └── database.properties
│   │
│   └── test/
│       └── java/
│           └── com/voting/system/
│               └── tests/
│
├── lib/
│   ├── mysql-connector-java.jar
│   └── other-dependencies.jar
│
├── README.md
└── pom.xml (or build.gradle)
```

## Dependencies

- MySQL Connector
- RSA Cryptography Libraries

## Configuration

1. Update `database.properties` with your MySQL credentials
2. Configure database connection in `DatabaseService`

## Security Features

- RSA Public/Private Key Pair Generation
- Vote Signature Verification
- Blockchain Integrity Checks
- Secure Password Hashing

## Running the Application

```bash
# Compile the project
mvn clean install

# Run the application
java -jar blockchain-voting.jar
```

## Usage Instructions

1. Register as an admin
2. Create elections
3. Add candidates
4. Generate voter IDs
5. Users can cast votes securely

## Security Considerations

- Implement additional network security
- Use secure, private database connections
- Regularly update cryptographic libraries

## Limitations

- Single-machine blockchain implementation
- No distributed consensus mechanism

## Future Improvements

- Distributed blockchain architecture
- Advanced cryptographic protocols
- Enhanced vote verification mechanisms
