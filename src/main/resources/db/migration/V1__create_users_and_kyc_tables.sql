CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    phone_number VARCHAR(50),
    profile_picture_url TEXT,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS kyc_submission (
    kyc_id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(user_id),
    full_name_ktp VARCHAR(255) NOT NULL,
    ktp_number CHAR(16) NOT NULL,
    ktp_photo_url TEXT NOT NULL,
    selfie_with_ktp_url TEXT NOT NULL,
    bio TEXT,
    status VARCHAR(50) NOT NULL,
    rejection_reason TEXT,
    reviewed_by UUID REFERENCES users(user_id),
    submitted_at TIMESTAMPTZ NOT NULL,
    reviewed_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS kyc_social_media_link (
    kyc_id UUID NOT NULL REFERENCES kyc_submission(kyc_id) ON DELETE CASCADE,
    platform VARCHAR(100),
    url TEXT,
    PRIMARY KEY (kyc_id, platform, url)
);

