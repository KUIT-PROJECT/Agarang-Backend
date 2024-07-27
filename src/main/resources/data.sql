-- -- Delete existing records
-- DELETE FROM memory_bookmark WHERE id=1;
-- DELETE FROM memory WHERE baby_id=1 AND member_id=1;
-- DELETE FROM member WHERE id=1;
-- DELETE FROM baby WHERE id=1;


-- -- Insert new baby record
-- INSERT INTO baby (id, character_id, code, name, expected_due_at, weight, created_at, updated_at)
-- VALUES (1, NULL, 'DXW1234', '아가', '2025-01-01', 1.8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- -- Insert new member record
-- INSERT INTO member (id, baby_id, refresh_token_id, role, created_at, updated_at)
-- VALUES (1, 1, NULL, '아빠', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- -- Insert new memory records
-- INSERT INTO memory (id, created_at, updated_at, status, genre, image_url, instrument, mood, tempo, text, baby_id, member_id)
-- VALUES
--     (1, '2024-07-01 00:00:00', '2024-07-01 00:00:00', 'ACTIVE', 'ACOUSTIC', 'https://example.com/image1.jpg', 'PIANO', 'HAPPY', 'FAST', 'Memory 1', 1, 1),
--     (2, '2024-07-02 00:00:00', '2024-07-02 00:00:00', 'ACTIVE', 'BALLAD', 'https://example.com/image2.jpg', 'BASE_GUITAR', 'BEAUTIFUL', 'MID', 'Memory 2', 1, 1),
--     (3, '2024-07-03 00:00:00', '2024-07-03 00:00:00', 'ACTIVE', 'ELECTRONIC', 'https://example.com/image3.jpg', 'VIOLIN', 'ENERGETIC', 'SLOW', 'Memory 3', 1, 1),
--     (4, '2024-07-04 00:00:00', '2024-07-04 00:00:00', 'ACTIVE', 'HIPHOP', 'https://example.com/image4.jpg', 'FLUTE', 'BRIGHT', 'FAST', 'Memory 4', 1, 1),
--     (5, '2024-07-05 00:00:00', '2024-07-05 00:00:00', 'ACTIVE', 'INDIE', 'https://example.com/image5.jpg', 'SAXOPHONE', 'LOVELY', 'MID', 'Memory 5', 1, 1),
--     (6, '2024-07-06 00:00:00', '2024-07-06 00:00:00', 'ACTIVE', 'JAZZ', 'https://example.com/image6.jpg', 'TRUMPET', 'FANTASTIC', 'SLOW', 'Memory 6', 1, 1),
--     (7, '2024-07-07 00:00:00', '2024-07-07 00:00:00', 'ACTIVE', 'POP', 'https://example.com/image7.jpg', 'VIOLA', 'PEACEFUL', 'FAST', 'Memory 7', 1, 1),
--     (8, '2024-07-08 00:00:00', '2024-07-08 00:00:00', 'ACTIVE', 'RNB', 'https://example.com/image8.jpg', 'CLARINET', 'TOUCHING', 'MID', 'Memory 8', 1, 1),
--     (9, '2024-07-09 00:00:00', '2024-07-09 00:00:00', 'ACTIVE', 'ROCK', 'https://example.com/image9.jpg', 'XYLOPHONE', 'WARM', 'SLOW', 'Memory 9', 1, 1),
--     (10, '2024-07-10 00:00:00', '2024-07-10 00:00:00', 'ACTIVE', 'ACOUSTIC', 'https://example.com/image10.jpg', 'HARP', 'HAPPY', 'FAST', 'Memory 10', 1, 1);

-- INSERT INTO memory_bookmark (id, created_at, updated_at, memory_id, member_id)
-- values (1,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1)
