-- Seed Data for Blockchain Voting System
-- Insert Default Admin User
INSERT INTO users (username, password_hash, role, voter_id, is_voter_id_valid, public_key, private_key) VALUES
(
'admin',
'Lt8tJjCMr1n/WdMTu/dyPUOoLB6Gzf9LvEe/NHUXV76RRacenXvxQKvgS+HQTX5a', -- password = secureAdminPassword123
'ADMIN',
'ADMIN-001',
true,
'DEFAULT_PUBLIC_KEY',
'DEFAULT_PRIVATE_KEY'
);
-- Insert Regular Test Users
INSERT INTO users (username, password_hash, role, voter_id, is_voter_id_valid, public_key, private_key) VALUES
('voter1', 'se5mjAn6mSZW/q94TWPgv8fs9jG2z+3wec3EFyFi9vtcqs54is5yLus+VQr/RphY', 'USER', 'VOTER-001', true, 'PUBLIC_KEY_1', 'PRIVATE_KEY_1'), -- password = voter1Password
('voter2', 'aoI4E+ZRfzolpieXlghAIzfsFw4IuxMtCGxcDBY6S6DNum7L4G9KTop0L5NAPeV0', 'USER', 'VOTER-002', true, 'PUBLIC_KEY_2', 'PRIVATE_KEY_2'), -- password = voter2Password
('voter3', 'krbESGWdJllubeFoc5XLCemYKU0DfURrjJJZ5PEOKtpfPYm4K6WUTsx3/rBR4hpu', 'USER', 'VOTER-003', true, 'PUBLIC_KEY_3', 'PRIVATE_KEY_3'); -- password = voter3Password
-- Create Test Elections
INSERT INTO elections (title, start_date, end_date, created_by) VALUES
('Presidential Election 2024', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1),
('City Council Election', '2024-03-01 00:00:00', '2024-03-31 23:59:59', 1);
-- Add Candidates to Elections
INSERT INTO candidates (name, election_id) VALUES
-- Presidential Election Candidates
('John Smith', 1),
('Emily Johnson', 1),
('Michael Brown', 1),
-- City Council Election Candidates
('Sarah Davis', 2),
('Robert Wilson', 2),
('Jennifer Lee', 2);
-- Optional: Seed some test votes (for demonstration)
INSERT INTO blocks (
previous_hash,
timestamp,
voter_id,
election_id,
candidate_id,
nonce,
hash,
signature
) VALUES
(
'0',
NOW(),
'VOTER-001',
1,
1,
12345,
'EXAMPLE_HASH',
'EXAMPLE_SIGNATURE'
);