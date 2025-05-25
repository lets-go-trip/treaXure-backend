-- Treaxure Database Data Insert Script (H2 Compatible Version)
-- Generated from RDS dump on 2025-05-23
-- This file contains only INSERT statements compatible with H2 database

-- Clear existing data first to avoid primary key conflicts
DELETE FROM vote;
DELETE FROM favorite;
DELETE FROM visit;
DELETE FROM board;
DELETE FROM mission;
DELETE FROM week;
DELETE FROM place;
DELETE FROM member;

-- Reset auto increment values using H2 syntax
ALTER TABLE member ALTER COLUMN member_id RESTART WITH 1;
ALTER TABLE place ALTER COLUMN place_id RESTART WITH 1;
ALTER TABLE week ALTER COLUMN week_id RESTART WITH 1;
ALTER TABLE mission ALTER COLUMN mission_id RESTART WITH 1;
ALTER TABLE board ALTER COLUMN board_id RESTART WITH 1;
ALTER TABLE visit ALTER COLUMN visit_id RESTART WITH 1;
ALTER TABLE favorite ALTER COLUMN favorite_id RESTART WITH 1;
ALTER TABLE vote ALTER COLUMN vote_id RESTART WITH 1;

-- Step 1: Insert base tables first (no FK dependencies)

-- Insert data for member table (base table)
INSERT INTO member (is_active, point, created_at, member_id, updated_at, email, nickname, password, profile_url, role) VALUES 
(1, 1000, '2025-05-23 02:35:00.000000', 1, '2025-05-23 02:35:00.000000', 'admin@treaxure.com', '관리자', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=2070&auto=format&fit=crop', 'ADMIN'),
(1, 750, '2025-05-23 02:35:00.000000', 2, '2025-05-23 02:35:00.000000', 'user1@example.com', '트레져헌터', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=2070&auto=format&fit=crop', 'USER'),
(1, 500, '2025-05-23 02:35:00.000000', 3, '2025-05-23 02:35:00.000000', 'user2@example.com', '여행매니아', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=2070&auto=format&fit=crop', 'USER'),
(1, 300, '2025-05-23 02:35:00.000000', 4, '2025-05-23 02:35:00.000000', 'user3@example.com', '서울탐험가', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=2187&auto=format&fit=crop', 'USER'),
(1, 920, '2025-05-23 02:35:00.000000', 5, '2025-05-23 02:35:00.000000', 'user4@example.com', '포토그래퍼', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1639149888905-fb39731f2e6c?q=80&w=1964&auto=format&fit=crop', 'USER'),
(1, 600, '2025-05-23 02:35:00.000000', 6, '2025-05-23 02:35:00.000000', 'user5@example.com', '미식가', '$2a$10$hcV7SbM/QR7RoHysZ7o1suw4ahBNmrKS9dq6rsFU4t.i3iHR6FHTi', 'https://images.unsplash.com/photo-1580489944761-15a19d654956?q=80&w=2022&auto=format&fit=crop', 'USER');

-- Insert data for place table (base table)
INSERT INTO place (is_active, latitude, longitude, place_id, created_at, updated_at, address, category, description, name, thumbnail_url) VALUES 
(1, 37.5065, 127.053, 1, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 강남구 테헤란로 521', '카페', '강남역 근처의 아늑한 카페입니다. 시그니처 커피와 수제 디저트가 유명합니다.', '트레저 카페', 'https://images.unsplash.com/photo-1554118811-1e0d58224f24?q=80&w=2047&auto=format&fit=crop'),
(1, 37.5635, 126.9252, 2, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 마포구 연남동 567-28', '레스토랑', '연남동의 인기 파스타 레스토랑입니다. 수제 파스타와 와인이 맛있습니다.', '연남 파스타', 'https://images.unsplash.com/photo-1481833761820-0509d3217039?q=80&w=2070&auto=format&fit=crop'),
(1, 37.5826, 126.9856, 3, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 종로구 계동길 37', '관광명소', '전통 한옥의 아름다움을 간직한 북촌한옥마을입니다. 한복 체험이 가능합니다.', '북촌 한옥마을', 'https://images.unsplash.com/photo-1549693578-d683be217e58?q=80&w=2035&auto=format&fit=crop'),
(1, 37.5352, 127.0094, 4, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 용산구 한남동 독서당로 122-1', '바', '한강이 보이는 루프탑 바입니다. 노을 시간대에 특히 아름다운 뷰를 자랑합니다.', '한강뷰 바', 'https://images.unsplash.com/photo-1470337458703-46ad1756a187?q=80&w=2069&auto=format&fit=crop'),
(1, 37.5637, 126.9821, 5, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 중구 명동길 74', '쇼핑', '명동의 중심가에 위치한 대형 쇼핑몰입니다. 다양한 브랜드와 먹거리가 있습니다.', '명동 쇼핑센터', 'https://images.unsplash.com/photo-1607083206968-13611e3d76db?q=80&w=2115&auto=format&fit=crop'),
(1, 37.5568, 126.9245, 6, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', '서울특별시 마포구 홍익로 3길', '엔터테인먼트', '젊음의 거리 홍대입니다. 다양한 클럽과 라이브 음악을 즐길 수 있습니다.', '홍대 클럽거리', 'https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?q=80&w=2070&auto=format&fit=crop');

-- Insert data for week table (base table)
INSERT INTO week (week_end, week_id, week_start) VALUES 
('2025-05-25', 1, '2025-05-19'),
('2025-06-01', 2, '2025-05-26'),
('2025-06-08', 3, '2025-06-02'),
('2025-06-15', 4, '2025-06-09');

-- Step 2: Insert tables with FK dependencies

-- Insert data for mission table (depends on: member, place) - using TRUE/FALSE instead of _binary
INSERT INTO mission (is_active, place_id, score, created_at, evaluated_at, member_id, mission_id, title, description, reference_url, status, type) VALUES 
(TRUE, 1, 50, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 1, 1, '카페 시그니처 메뉴 인증', '트레저 카페에서 시그니처 메뉴를 주문하고 인증샷을 찍어보세요!', 'https://images.unsplash.com/photo-1445116572660-236099ec97a0?q=80&w=2071&auto=format&fit=crop', 'APPROVED', 'PHOTO'),
(TRUE, 2, 30, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 2, 2, '파스타 먹기 미션', '연남 파스타에서 파스타를 먹고 리뷰를 남겨보세요.', 'https://images.unsplash.com/photo-1473093295043-cdd812d0e601?q=80&w=2070&auto=format&fit=crop', 'APPROVED', 'VISIT'),
(TRUE, 3, 100, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 3, 3, '한옥 전통의상 인증', '북촌 한옥에서 전통의상을 입고 사진을 찍어보세요.', 'https://images.unsplash.com/photo-1549693578-d683be217e58?q=80&w=2035&auto=format&fit=crop', 'PENDING', 'POSE'),
(TRUE, 4, 80, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 4, 4, '노을 칵테일 미션', '한강뷰 바에서 칵테일을 마시며 노을 사진을 찍어보세요.', 'https://images.unsplash.com/photo-1560472354-b33ff0c44a43?q=80&w=2126&auto=format&fit=crop', 'APPROVED', 'PHOTO'),
(FALSE, 5, 60, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 5, 5, '쇼핑 인증 미션', '명동 쇼핑센터에서 쇼핑 후 구매한 물건을 인증해보세요.', 'https://images.unsplash.com/photo-1483985988355-763728e1935b?q=80&w=2070&auto=format&fit=crop', 'REJECTED', 'PHOTO'),
(TRUE, 6, 70, '2025-05-23 02:35:00.000000', '2025-05-23 02:35:00.000000', 2, 6, '홍대 라이브 미션', '홍대에서 라이브 음악을 감상하고 분위기 사진을 찍어보세요.', 'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?q=80&w=2070&auto=format&fit=crop', 'APPROVED', 'PHOTO');

-- Insert data for board table (depends on: mission)
INSERT INTO board (board_id, favorite_count, is_active, created_at, member_id, mission_id, image_url, title) VALUES 
(1, 5, 1, '2025-05-23 02:35:00.000000', 1, 1, 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=2070&auto=format&fit=crop', '트레저 카페 후기'),
(2, 3, 1, '2025-05-23 02:35:00.000000', 2, 2, 'https://images.unsplash.com/photo-1565299507177-b0ac66763828?q=80&w=1922&auto=format&fit=crop', '연남동 맛집 추천'),
(3, 8, 1, '2025-05-23 02:35:00.000000', 3, 3, 'https://korean.visitseoul.net/data/kukudocs/seoul2133/20220518/202205181617250471.jpg', '북촌 한옥 체험기'),
(4, 10, 1, '2025-05-23 02:35:00.000000', 4, 4, 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?q=80&w=2070&auto=format&fit=crop', '한강 노을 명소'),
(5, 4, 1, '2025-05-23 02:35:00.000000', 5, 5, 'https://images.unsplash.com/photo-1542838132-92c53300491e?q=80&w=1974&auto=format&fit=crop', '명동 쇼핑 후기'),
(6, 7, 1, '2025-05-23 02:35:00.000000', 6, 6, 'https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?q=80&w=2070&auto=format&fit=crop', '홍대 라이브 후기');

-- Insert data for visit table (depends on: member, place)
INSERT INTO visit (place_id, visit_count, visit_id, created_at, member_id, updated_at) VALUES 
(1, 2, 1, '2025-05-23 02:35:00.000000', 1, '2025-05-23 02:35:00.000000'),
(1, 1, 2, '2025-05-23 02:35:00.000000', 3, '2025-05-23 02:35:00.000000'),
(2, 1, 3, '2025-05-23 02:35:00.000000', 2, '2025-05-23 02:35:00.000000'),
(2, 3, 4, '2025-05-23 02:35:00.000000', 4, '2025-05-23 02:35:00.000000'),
(3, 1, 5, '2025-05-23 02:35:00.000000', 3, '2025-05-23 02:35:00.000000'),
(4, 2, 6, '2025-05-23 02:35:00.000000', 4, '2025-05-23 02:35:00.000000'),
(5, 1, 7, '2025-05-23 02:35:00.000000', 5, '2025-05-23 02:35:00.000000'),
(6, 4, 8, '2025-05-23 02:35:00.000000', 2, '2025-05-23 02:35:00.000000'),
(6, 2, 9, '2025-05-23 02:35:00.000000', 6, '2025-05-23 02:35:00.000000');

-- Step 3: Insert tables with multiple FK dependencies (must be last)

-- Insert data for favorite table (depends on: board, member)
INSERT INTO favorite (board_id, created_at, favorite_id, member_id) VALUES 
(1, '2025-05-23 02:35:00.000000', 1, 2),
(1, '2025-05-23 02:35:00.000000', 2, 3),
(1, '2025-05-23 02:35:00.000000', 3, 4),
(2, '2025-05-23 02:35:00.000000', 4, 3),
(2, '2025-05-23 02:35:00.000000', 5, 4),
(3, '2025-05-23 02:35:00.000000', 6, 1),
(3, '2025-05-23 02:35:00.000000', 7, 2),
(3, '2025-05-23 02:35:00.000000', 8, 5),
(4, '2025-05-23 02:35:00.000000', 9, 1),
(4, '2025-05-23 02:35:00.000000', 10, 2),
(4, '2025-05-23 02:35:00.000000', 11, 3),
(5, '2025-05-23 02:35:00.000000', 12, 6),
(6, '2025-05-23 02:35:00.000000', 13, 1),
(6, '2025-05-23 02:35:00.000000', 14, 3);

-- Insert data for vote table (depends on: board, member, week)
INSERT INTO vote (board_id, week_id, member_id, vote_id, field) VALUES 
(1, 1, 2, 1, 'UP'),
(1, 1, 3, 2, 'UP'),
(2, 1, 4, 3, 'UP'),
(3, 1, 1, 4, 'UP'),
(3, 1, 5, 5, 'DOWN'),
(4, 1, 2, 6, 'UP'),
(4, 1, 6, 7, 'UP'),
(5, 1, 3, 8, 'DOWN'),
(6, 2, 1, 9, 'UP'),
(6, 2, 4, 10, 'UP');

-- Commit transaction
COMMIT; 