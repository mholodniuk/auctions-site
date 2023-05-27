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
                                   final_price numeric(7, 2) NOT NULL,
                                   FOREIGN KEY (seller_id) REFERENCES users(id),
                                   FOREIGN KEY (winner_id) REFERENCES users(id),
                                   FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE or replace TABLE watchlist (
            auction_id INT NOT NULL,
            user_id INT NOT NULL,
            relation_type ENUM('FOLLOWING', 'BIDING', 'SELLING') NOT NULL,
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            FOREIGN KEY (auction_id) REFERENCES auctions(id) ON DELETE CASCADE
        );
