-- Members 데이터
INSERT INTO public.members (is_blacklisted, created_at, deleted_at, id, last_login, updated_at,
                            email, login_type, name, nickname, password,
                            profile_image, provider, role, status)
VALUES
    (false, '2025-06-30 10:48:44.000000 +00:00', null, 1, null, null, 'aaa@aaa.com',
     'LOCAL', '노선우', '노션우',
     '{bcrypt}$2a$10$.RttUQ8agUixVeg3UiLkJeboPyG9hq7EkEUZTOpPKpvW8K1oUKLRG',
     null, null, 'ROLE_USER', '2025-07-11 09:16:33.910784'),

    (false, '2025-07-11 09:16:33.910752 +00:00', null, 2, null, null, 'bbb@aaa.com',
     'LOCAL', '임서현', '임세령',
     '{bcrypt}$2a$10$.RttUQ8agUixVeg3UiLkJeboPyG9hq7EkEUZTOpPKpvW8K1oUKLRG',
     null, null, 'ROLE_USER', '2025-07-11 09:16:33.910784'),

    (true, '2025-07-13 14:03:48.420649 +00:00', null, 3, null, '2025-07-13 14:03:48.420668 +00:00',
     'ccc@ccc.com', 'LOCAL', '김나단', '김가나단',
     '{bcrypt}$2a$10$n/PDTvgDuoFbovmzOAxstOuXFA2NWLQDAy/BsIm1yNE.IWwjM60Yu',
     null, null, 'ROLE_USER', 'ACTIVE'),

    (false, '2025-07-13 14:08:10.200440 +00:00', null, 4, null, '2025-07-13 14:08:10.201013 +00:00',
     'ddd@ddd.com', 'LOCAL', '이초롱', '롱롱',
     '{bcrypt}$2a$10$tdt3EceYdja2K4WekvZgweOBOKt0x.Yz2Gka7wB.biQ1..jo1mJ.m',
     null, null, 'ROLE_ADMIN', 'ACTIVE'),

    (true, '2025-07-13 14:08:43.742824 +00:00', null, 5, null, '2025-07-13 14:08:43.742848 +00:00',
     'cocjfals0@gmail.com', 'SOCIAL', 'chulmin chae', 'google_381267',
     'e745e950-929c-4b6b-bf74-eab2814ed577',
     'https://lh3.googleusercontent.com/a/ACg8ocItiSYT_99SLb-Jd99ISYCFhKMx2O0SngxUbvQDZA4cCv635w=s96-c',
     'google', 'ROLE_USER', 'ACTIVE');


-- Category 데이터
INSERT INTO public.categories (created_at, id, category_image, description, name)
VALUES ('2025-07-11 10:03:28.000000 +00:00', 1, null, '내가 이 구역 맛잘알', '음식'),
       ('2025-07-11 10:03:39.000000 +00:00', 2, null, '한국사 덕후들 모여라', '한국사'),
       ('2025-07-11 10:04:03.000000 +00:00', 3, null, '삼국지 덕후들 모여라', '삼국지'),
       ('2025-07-11 10:04:15.000000 +00:00', 4, null, '나만의 야경 맛집을 공유해보자', '야경'),
       ('2025-07-11 10:55:01.000000 +00:00', 5, null, '나만의 여행 로드맵을 공유해보자', '여행');

-- Roadmaps 데이터
INSERT INTO public.roadmaps (is_animated, is_public, like_count, view_count, category_id,
                             created_at, deleted_at, id, member_id, original_roadmap_id, updated_at,
                             description, roadmap_type, thumbnail, title)
VALUES (true, true, 15, 70, 1, '2025-07-11 10:46:52.000000 +00:00', null, 1, 1, null, null,
        '잠실 주변 맛집 내가 추천해줄게', 'PERSONAL', null, '잠실 주변 맛집'),
       (true, true, 35, 100, 2, '2025-07-11 10:51:46.000000 +00:00', null, 2, 1, null, null,
        '서울시와 함께 하는 야경 맛집 스팟', 'SHARED', null, '서울시 야경 맛집'),
       (true, false, 1, 3, 3, '2025-07-11 10:53:21.000000 +00:00', null, 3, 1, null, null,
        '8/11-8/12 여름 삿포로 여행', 'PERSONAL', null, '여름 일본 여행'),
       (false, true, 3, 45, 2, '2025-07-13 17:42:59.736000 +00:00', null, 4, 2, null, null,
        '이번 겨울 온천여행은 여기로~!', 'PERSONAL', null, '겨울 온천 여행'),
       (false, false, 86, 18, 1, '2025-07-13 17:05:00.000000+00:00', null, 5, 3, null, null,
        '설명 5번 - 자동 생성된 설명입니다.', 'PERSONAL', null, '로드맵 5번 타이틀'),
       (false, false, 5, 238, 3, '2025-07-13 17:06:00.000000+00:00', null, 6, 1, null, null,
        '설명 6번 - 자동 생성된 설명입니다.', 'SHARED', null, '로드맵 6번 타이틀'),
       (true, true, 92, 101, 4, '2025-07-13 17:07:00.000000+00:00', null, 7, 2, null, null,
        '설명 7번 - 자동 생성된 설명입니다.', 'PERSONAL', null, '로드맵 7번 타이틀'),
       (false, true, 0, 164, 2, '2025-07-13 17:08:00.000000+00:00', null, 8, 1, null, null,
        '설명 8번 - 자동 생성된 설명입니다.', 'SHARED', null, '로드맵 8번 타이틀');

-- Hashtags 데이터
INSERT INTO public.hashtags (created_at, id, name)
VALUES ('2025-07-11 10:45:28+00', 1, '서울'),
       ('2025-07-11 10:46:02+00', 2, '고구려'),
       ('2025-07-11 10:46:32+00', 3, '아경'),
       ('2025-07-11 10:51:12+00', 4, '맛집추천'),
       ('2025-07-11 10:56:44+00', 5, '삿포로'),
       ('2025-07-13 18:00:00+00', 6, '한식'),
       ('2025-07-13 18:00:00+00', 7, '데이트'),
       ('2025-07-13 18:00:00+00', 8, '전통시장'),
       ('2025-07-13 18:00:00+00', 9, '야경맛집'),
       ('2025-07-13 18:00:00+00', 10, '온천'),
       ('2025-07-13 18:00:00+00', 11, '일본'),
       ('2025-07-13 18:00:00+00', 12, '중국집'),
       ('2025-07-13 18:00:00+00', 13, '미식회'),
       ('2025-07-13 18:00:00+00', 14, '서울여행'),
       ('2025-07-13 18:00:00+00', 15, '성수동'),
       ('2025-07-13 18:00:00+00', 16, '도쿄');

-- Roadmap-Hashtag-relations 데이터
INSERT INTO public.roadmap_hashtag_relations (created_at, hashtag_id, id, roadmap_id)
VALUES ('2025-07-11 10:50:25+00', 1, 1, 1),
       ('2025-07-11 10:50:42+00', 4, 2, 1),
       ('2025-07-13 17:00:00+00', 4, 3, 1),
       ('2025-07-13 17:00:00+00', 15, 4, 1),
       ('2025-07-13 17:00:00+00', 10, 5, 1),
       ('2025-07-13 18:01:00+00', 3, 6, 2),
       ('2025-07-13 18:01:00+00', 9, 7, 2),
       ('2025-07-13 18:01:00+00', 1, 8, 3),
       ('2025-07-13 18:01:00+00', 11, 9, 3),
       ('2025-07-13 18:01:00+00', 10, 10, 4),
       ('2025-07-13 18:01:00+00', 7, 11, 5),
       ('2025-07-13 18:01:00+00', 6, 12, 6),
       ('2025-07-13 18:01:00+00', 13, 13, 6),
       ('2025-07-13 18:01:00+00', 14, 14, 7),
       ('2025-07-13 18:01:00+00', 15, 15, 7),
       ('2025-07-13 18:01:00+00', 2, 16, 8);

-- Bookmarks 데이터
INSERT INTO public.bookmarks (created_at, id, member_id, roadmap_id)
VALUES ('2025-07-11 11:24:43+00', 1, 1, 1),
       ('2025-07-13 18:05:00+00', 2, 2, 2),
       ('2025-07-13 18:05:10+00', 3, 3, 3),
       ('2025-07-13 18:05:20+00', 4, 4, 1),
       ('2025-07-13 18:05:30+00', 5, 5, 2),
       ('2025-07-13 18:05:40+00', 6, 1, 3);

-- Layers 데이터
INSERT INTO public.layers (layer_seq, layer_time, created_at, deleted_at, id, member_id, roadmap_id,
                           updated_at, description, name)
VALUES (1, '2025-07-13', '2025-07-13 16:23:21.552000 +00:00', null, 1, 1, 1, null, null, '롯데월드몰'),
       (2, null, '2025-07-13 16:24:06.215000 +00:00', null, 2, 1, 1, null, null, '방이먹자골목'),
       (1, '2025-07-14', '2025-07-14 09:00:00+00', NULL, 3, 2, 2, NULL, '강남 일대 소개', '강남 탐방'),
       (2, '2025-07-14', '2025-07-14 09:10:00+00', NULL, 4, 3, 3, NULL, '서울 야경 코스', '야경 투어'),
       (1, '2025-07-14', '2025-07-14 09:20:00+00', NULL, 5, 4, 4, NULL, '겨울 온천 지역', '온천 가이드'),
       (2, NULL, '2025-07-14 09:30:00+00', NULL, 6, 1, 1, NULL, '먹자골목 정리', '잠실 맛집'),
       (3, NULL, '2025-07-14 09:40:00+00', NULL, 7, 2, 2, NULL, '데이트 코스 추천', '서울 데이트');

-- Markers 데이터
INSERT INTO public.markers (lat, lng, marker_seq, created_at, deleted_at, id, layer_id, member_id,
                            updated_at, color, description, image_url, name)
VALUES (37.512817, 127.102496, 1, '2025-07-13 16:28:29.510000 +00:00', null, 1, 1, 1, null,
        '#FF0000', '잠실 제일가는 함박 스테이크', null, '후쿠오카 함바그 롯데월드몰점'),
       (37.512817, 127.102496, 2, '2025-07-13 16:34:33.283000 +00:00', null, 2, 1, 1, null,
        '#FF0000', '버거하면 고든램지', null, '고든램지버거 롯데월드몰점'),
       (37.498095, 127.027610, 1, '2025-07-14 10:00:00+00', NULL, 3, 3, 2, NULL, '#00FF00',
        '강남역 핫플', NULL, '카페 드롭탑'),
       (37.497950, 127.027600, 2, '2025-07-14 10:10:00+00', NULL, 4, 3, 2, NULL, '#0000FF',
        '분위기 좋은 라멘집', NULL, '멘쇼 강남점'),
       (37.516512, 127.100182, 1, '2025-07-14 10:20:00+00', NULL, 5, 4, 3, NULL, '#FFA500',
        '서울야경 포인트', NULL, '몽마르뜨 공원'),
       (37.337497, 127.009283, 1, '2025-07-14 10:30:00+00', NULL, 6, 5, 4, NULL, '#800080',
        '유명한 온천 마을', NULL, '수안보 온천'),
       (37.512817, 127.102496, 3, '2025-07-14 10:40:00+00', NULL, 7, 6, 1, NULL, '#008080',
        '야시장 분위기', NULL, '방이전통시장');

-- LayerLibraries 데이터
INSERT INTO public.layer_libraries (created_at, id, layer_id, member_id)
VALUES ('2025-07-13 17:19:57.332000 +00:00', 1, 1, 2),
       ('2025-07-13 17:20:10.760000 +00:00', 2, 2, 2);

-- Quest 데이터
INSERT INTO quests (is_active, completed_at, created_at, deleted_at, id, member_id, updated_at,
                    description, quest_image, title)
VALUES (true, null, '2025-07-13 19:00:00+00:00', null, 1, 1, null,
        '여기 어디게~? 2탄 장소에 대한 퀘스트입니다. 사진을 보고 맞혀보세요!', 'https://example.com/q2.jpg', '여기 어디게~? 2탄'),
       (true, null, '2025-07-13 19:01:00+00:00', null, 2, 3, null,
        '여기 어디게~? 3탄 장소에 대한 퀘스트입니다. 사진을 보고 맞혀보세요!', 'https://example.com/q3.jpg', '여기 어디게~? 3탄'),
       (true, null, '2025-07-13 19:02:00+00:00', null, 3, 2, null,
        '여기 어디게~? 4탄 장소에 대한 퀘스트입니다. 사진을 보고 맞혀보세요!', 'https://example.com/q4.jpg', '여기 어디게~? 4탄');

-- MemberQuest 데이터
INSERT INTO member_quests (completed_at, created_at, deleted_at, id, member_id, quest_id,
                           updated_at, answer, is_recognized, status)
VALUES (null, '2025-07-13 19:00:00+00:00', null, 3, 2, 2, null, '롯데월드몰', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:00:00+00:00', null, 4, 5, 2, null, '남산타워', 'false', 'WRONG'),
       (null, '2025-07-13 19:00:00+00:00', null, 5, 1, 2, null, '서울숲', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:01:00+00:00', null, 6, 3, 3, null, '방이먹자골목', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:01:00+00:00', null, 7, 2, 3, null, '성수동카페', 'false', 'WRONG'),
       (null, '2025-07-13 19:01:00+00:00', null, 8, 4, 3, null, '롯데월드몰', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:02:00+00:00', null, 9, 5, 2, null, '남산타워', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:02:00+00:00', null, 10, 1, 1, null, '롯데월드몰', 'true', 'COMPLETED'),
       (null, '2025-07-13 19:02:00+00:00', null, 11, 2, 1, null, '서울숲', 'false', 'WRONG');

-- QuestRank 데이터
INSERT INTO quest_ranks (rank, completed_at, created_at, deleted_at, id, member_id, quest_id,
                         updated_at)
VALUES (1, null, '2025-07-13 19:00:00+00:00', null, 3, 2, 2, null),
       (1, null, '2025-07-13 19:00:00+00:00', null, 4, 1, 2, null),
       (1, null, '2025-07-13 19:01:00+00:00', null, 5, 3, 3, null),
       (1, null, '2025-07-13 19:01:00+00:00', null, 6, 4, 3, null),
       (1, null, '2025-07-13 19:02:00+00:00', null, 7, 5, 1, null),
       (1, null, '2025-07-13 19:02:00+00:00', null, 8, 1, 1, null);

-- MemberQuestEvidences 데이터
INSERT INTO member_quest_evidences (created_at, deleted_at, id, member_quest_id, updated_at,
                                    description, image_url)
VALUES ('2025-07-13 19:00:00+00:00', null, 3, 3, null, '현장 인증입니다.',
        'https://example.com/evidence3.jpg'),
       ('2025-07-13 19:00:00+00:00', null, 4, 4, null, '잘 모르겠지만 찍었어요.',
        'https://example.com/evidence4.jpg'),
       ('2025-07-13 19:00:00+00:00', null, 5, 5, null, '서울숲일지도?',
        'https://example.com/evidence5.jpg'),
       ('2025-07-13 19:01:00+00:00', null, 6, 6, null, '이 골목 어디게?',
        'https://example.com/evidence6.jpg'),
       ('2025-07-13 19:01:00+00:00', null, 7, 7, null, '찍긴 했는데요...',
        'https://example.com/evidence7.jpg'),
       ('2025-07-13 19:01:00+00:00', null, 8, 8, null, '이 장소 아닐까요?',
        'https://example.com/evidence8.jpg'),
       ('2025-07-13 19:02:00+00:00', null, 9, 9, null, '온천 근처입니다.',
        'https://example.com/evidence9.jpg'),
       ('2025-07-13 19:02:00+00:00', null, 10, 10, null, '정답 맞죠?',
        'https://example.com/evidence10.jpg'),
       ('2025-07-13 19:02:00+00:00', null, 11, 11, null, '틀려도 재미있었어요~',
        'https://example.com/evidence11.jpg');

-- Reports 데이터
INSERT INTO "public"."reports" (id, reporter_id, reported_member_id, roadmap_id, marker_id,
                                quest_id, created_at, resolved_at, description, status)
VALUES (1, 2, 1, 1, NULL, NULL, '2024-07-13 00:15:05.732000 +00:00', NULL, '잘못된 정보', 'REPORTED'),
       (2, 3, 2, 2, NULL, NULL, '2025-07-14 09:00:00+00', NULL, '스팸성 콘텐츠입니다.', 'IN_PROGRESS'),
       (3, 1, 3, NULL, 1, NULL, '2025-07-14 10:15:00+00', NULL, '장소가 잘못 지정됨', 'IN_PROGRESS'),
       (4, 2, 1, 3, NULL, NULL, '2025-07-14 11:30:00+00', NULL, '무단 광고 포함됨', 'IN_PROGRESS'),
       (5, 5, 4, 4, NULL, NULL, '2025-07-14 12:45:00+00', NULL, '설명이 부적절함', 'RESOLVED'),
       (6, 4, 2, NULL, 2, NULL, '2025-07-14 13:15:00+00', NULL, '마커가 실제 장소와 다름', 'RESOLVED');

-- Comments 데이터
INSERT INTO "public"."comments" (created_at, id, member_id, roadmap_id, quest_id, content)
VALUES ('2025-07-13 15:14:28.674000 +00:00', 2, 2, 1, null, '오 이거 괜찮네'),
       ('2025-07-14 17:46:42.166000 +00:00', 1, 1, null, 2, '하이'),
       ('2025-07-14 10:00:00+00', 3, 3, 1, NULL, '여기 꼭 가보고 싶네요!'),
       ('2025-07-14 10:05:00+00', 4, 1, NULL, 2, '퀘스트가 정말 흥미롭습니다'),
       ('2025-07-14 10:10:00+00', 5, 2, 3, NULL, '일정에 참고할게요!'),
       ('2025-07-14 10:15:00+00', 6, 4, 2, NULL, '정말 유용한 정보 감사합니다'),
       ('2025-07-14 10:20:00+00', 7, 5, NULL, 1, '재미있는 퀘스트였습니다');