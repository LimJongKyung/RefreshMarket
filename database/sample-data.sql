INSERT INTO member (
    id, passwd, name, registrationnumber, address, phonenumber, email, grade, point
) VALUES (
    'portfolio', 'portfolio1234', '포트폴리오 사용자', NULL,
    '서울특별시', '010-0000-0000', 'portfolio@example.com', 'GOLD', 5000
);

INSERT INTO member_benefit (member_id, benefit_name, benefit_description)
VALUES ('portfolio', '포트폴리오 체험', '샘플 계정용 무료 배송 혜택');

INSERT INTO menu (id, name, position) VALUES (1, '신선식품', 'header');
INSERT INTO menu (id, name, position) VALUES (2, '가공식품', 'header');
INSERT INTO menu (id, name, position) VALUES (3, '오늘의 세일', 'sidebar');
INSERT INTO menu (id, name, position) VALUES (4, '공동구매', 'sidebar');
INSERT INTO menu (id, name, position) VALUES (5, '중소기업 제품', 'sidebar');

INSERT INTO products (
    product_id, name, description, price, stock, category, category_id,
    main_display, seller, manufacturer, detail_option, detail_option_price
) VALUES (
    1, '당일 수확 사과', '산지에서 당일 수확한 포트폴리오 샘플 상품입니다.',
    12900, 50, '신선식품', 'fresh', 'sale', '리프레시마켓', '샘플농원',
    '1kg,2kg,3kg', '0,9000,17000'
);

INSERT INTO products (
    product_id, name, description, price, stock, category, category_id,
    main_display, seller, manufacturer, detail_option, detail_option_price
) VALUES (
    2, '한우 공동구매 세트', '공동구매 기능을 보여주기 위한 샘플 상품입니다.',
    39900, 30, '신선식품', 'fresh', 'group', '리프레시마켓', '샘플축산',
    '기본,프리미엄', '0,20000'
);

INSERT INTO products (
    product_id, name, description, price, stock, category, category_id,
    main_display, seller, manufacturer
) VALUES (
    3, '수제 과일잼', '중소기업 제품 홍보 영역에 표시되는 샘플 상품입니다.',
    8900, 100, '가공식품', 'processed', 'promotion', '리프레시마켓', '샘플푸드'
);

INSERT INTO posts (
    post_id, title, content, member_id, created_date, updated_date, view_count, board_type
) VALUES (
    1, 'RefreshMarket 포트폴리오에 오신 것을 환영합니다.',
    '이 게시물은 배포 확인을 위한 샘플 데이터입니다.',
    'portfolio', SYSTIMESTAMP, SYSTIMESTAMP, 0, 'NOTICE'
);

INSERT INTO review (
    review_id, product_id, review_comment, created_at, user_id, user_name, rating
) VALUES (
    1, 1, '상품 상세와 리뷰 기능 확인을 위한 샘플 리뷰입니다.',
    SYSTIMESTAMP, 'portfolio', '포트폴리오 사용자', 5
);

COMMIT;
