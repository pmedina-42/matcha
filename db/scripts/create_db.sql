-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    "userName" VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    "lastName" VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INTEGER CHECK (age >= 0),
    gender VARCHAR(6),
    "sexualOrientation" VARCHAR(6),
    biography TEXT,
    interests TEXT[],
    "isVerified" BOOLEAN DEFAULT FALSE,
    location VARCHAR(255),
    "verificationToken" UUID UNIQUE,
    role VARCHAR(5) DEFAULT 'USER'
);

-- Create the logs table
CREATE TABLE IF NOT EXISTS logs (
    id SERIAL PRIMARY KEY,
    type VARCHAR(4) NOT NULL,
    sender VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    receiver VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the reports table
CREATE TABLE IF NOT EXISTS reports (
    id SERIAL PRIMARY KEY,
    reporter VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    reported VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    reason TEXT NOT NULL,
    solved BOOLEAN DEFAULT FALSE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the messages table
CREATE TABLE IF NOT EXISTS messages (
    id SERIAL PRIMARY KEY,
    sender VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    receiver VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the matches table
CREATE TABLE IF NOT EXISTS matches (
    id SERIAL PRIMARY KEY,
    user1 VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    user2 VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE matches ADD CONSTRAINT unique_match_pair UNIQUE (user1, user2);

-- Create the images table
CREATE TABLE IF NOT EXISTS images (
    id SERIAL PRIMARY KEY,
    "userName" VARCHAR(50) REFERENCES users("userName") ON DELETE CASCADE,
    isProfilePic BOOLEAN DEFAULT FALSE
);