-- tabelki
CREATE TABLE item_category (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               name varchar(32) NOT NULL
);

CREATE TABLE shipping_address (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  country varchar(20) NOT NULL,
                                  city varchar(32) NOT NULL,
                                  street_number varchar(20) NOT NULL,
                                  street_name varchar(20) NOT NULL,
                                  zip_code varchar(20) NOT NULL
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username varchar(32) NOT NULL,
                       PASSWORD varchar(16) NOT NULL,
                       email varchar(32) NOT NULL,
                       first_name varchar(32) NOT NULL,
                       last_name varchar(32) NOT NULL,
                       role ENUM ('ADMIN', 'USER') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       modified_at TIMESTAMP,
                       is_blocked BOOLEAN NOT NULL,
                       address_id INT NOT NULL,
                       FOREIGN KEY (address_id) REFERENCES shipping_address (id)
);

CREATE TABLE items (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name varchar(32) NOT NULL,
                       description text,
                       image_url varchar(64) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       modified_at TIMESTAMP,
                       category_id INT NOT NULL,
                       FOREIGN KEY (category_id) REFERENCES item_category (id)
);

CREATE TABLE auctions (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          item_id INT NOT NULL,
                          seller_id INT NOT NULL,
                          item_quantity INT DEFAULT 1,
                          starting_price numeric(7, 2) NOT NULL,
                          buy_now_price numeric(7, 2) NOT NULL,
                          current_bid numeric(7, 2),
                          current_bid_user_id INT,
                          expiration_date TIMESTAMP NOT NULL,
                          FOREIGN KEY (item_id) REFERENCES items(id),
                          FOREIGN KEY (seller_id) REFERENCES users(id),
                          FOREIGN KEY (current_bid_user_id) REFERENCES users(id)
);

CREATE TABLE finished_auctions (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   seller_id INT NOT NULL,
                                   item_id INT NOT NULL,
                                   winner_id INT,
                                   finished_at TIMESTAMP NOT NULL,
                                   item_quantity INT NOT NULL,
                                   final_price numeric(7, 2) NOT NULL
);

CREATE TABLE watchlist (
                           auction_id INT NOT NULL,
                           user_id INT NOT NULL,
                           relation_type ENUM('FOLLOWING', 'BIDING', 'SELLING') NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (auction_id) REFERENCES auctions(id)
);

CREATE INDEX auctions_buy_now_price ON auctions (buy_now_price);

CREATE INDEX auctions_current_bid ON auctions (current_bid);

CREATE INDEX auctions_expiration_date ON auctions (expiration_date);

CREATE INDEX item_category_name ON item_category (name);

CREATE INDEX items_name ON items (name);

CREATE INDEX users_username ON users (username);

CREATE INDEX users_email ON users (email);

CREATE INDEX users_role ON users (role);

-- widoki
CREATE
    OR REPLACE VIEW user_info_v AS
SELECT
    u.id AS user_id,
    u.username AS username,
    concat(u.first_name, ' ', u.last_name) AS full_name,
    u.email AS email,
    u.role AS ROLE,
    sa.country AS country,
    sa.city AS city,
    sa.street_name AS street,
    sa.street_number AS street_number
FROM
    users u
        LEFT JOIN shipping_address sa ON u.address_id = sa.id;

CREATE
    OR REPLACE VIEW auction_v AS
SELECT
    a .id as auction_id,
    a .current_bid_user_id,
    a .seller_id,
    a .buy_now_price,
    a .starting_price,
    a .current_bid,
    a .expiration_date,
    a .item_quantity,
    i.name,
    i.description,
    i.image_url,
    ic.name AS category
FROM
    auctions a
        LEFT JOIN items i ON a .item_id = i.id
        LEFT JOIN item_category ic ON i.category_id = ic.id;

CREATE VIEW active_auctions_v AS
SELECT
    a.*,
    u_seller.username AS seller,
    u_seller.email AS seller_email,
    u_buyer.username AS buyer,
    u_buyer.email AS buyer_email
FROM
    auction_v a
        LEFT JOIN user_info_v u_seller ON a .seller_id = u_seller.user_id
        LEFT JOIN user_info_v u_buyer ON a .current_bid_user_id = u_buyer.user_id;

-- triggery
CREATE TRIGGER validate_bid BEFORE
    UPDATE
    ON auctions FOR EACH ROW
BEGIN
IF NEW.current_bid IS NOT NULL AND NEW.current_bid <= OLD.current_bid THEN
		SIGNAL SQLSTATE '45000'
SET
    MESSAGE_TEXT = 'The updated current_bid must be greater than the existing current_bid.';
END IF;
END;

CREATE TRIGGER add_auction_to_watchlist_on_bid_update
    AFTER UPDATE ON auctions
    FOR EACH ROW
BEGIN
IF NEW.current_bid_user_id IS NOT NULL AND NEW.current_bid_user_id <> OLD.current_bid_user_id THEN
		CALL add_auction_to_watchlist (NEW.current_bid_user_id, NEW.id, 'BIDING');
END IF;
END;

CREATE TRIGGER add_auction_to_watchlist_on_sell_insert
    AFTER INSERT ON auctions
    FOR EACH ROW
BEGIN
CALL add_auction_to_watchlist (NEW.seller_id, NEW.id, 'SELLING');
END;

-- funkcje/procedury
CREATE FUNCTION add_auction_to_watchlist (user_id INT, auction_id INT, relation_type ENUM ('FOLLOWING', 'BIDING', 'SELLING'))
    RETURNS INT
BEGIN
    INSERT INTO watchlist (user_id, auction_id, relation_type)
    VALUES(user_id, auction_id, relation_type);
    RETURN LAST_INSERT_ID();
END;


CREATE FUNCTION move_auction_to_finished (auction_id INT, seller_id INT, winner_id INT, item_quantity INT, final_price DECIMAL (7, 2))
    RETURNS INT
BEGIN
    INSERT INTO finished_auctions (seller_id, item_id, winner_id, finished_at, item_quantity, final_price)
    SELECT
        seller_id,
        item_id,
        winner_id,
        NOW(),
        item_quantity,
        final_price
    FROM
        auctions
    WHERE
            id = auction_id;
    DELETE FROM auctions
    WHERE id = auction_id;
    RETURN ROW_COUNT();
END;


CREATE PROCEDURE buy_now (auction_id INT, user_id INT)
BEGIN
    DECLARE
        seller_id INT;
        DECLARE
        item_quantity INT;
        DECLARE
        final_price DECIMAL (7,2);
        SELECT seller_id, item_quantity, buy_now_price INTO seller_id, item_quantity, final_price
	FROM
		auctions
	WHERE
		id = auction_id;
        CALL move_auction_to_finished (auction_id, seller_id, user_id, item_quantity, final_price);
        END;


        CREATE PROCEDURE place_bid (auction_id INT, user_id INT, bid_value DECIMAL (7, 2))
    BEGIN
        UPDATE
            auctions SET
            current_bid_user_id = user_id AND current_bid = bid_value
        WHERE
                id = auction_id;
    END;