CREATE TABLE archive (
                           `archive_id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `author` varchar(255) DEFAULT NULL,
                           `deleted_at` datetime(6) DEFAULT NULL,
                           `heart_cnt` bigint(20) NOT NULL,
                           `isbn` varchar(255) DEFAULT NULL,
                           `publisher` varchar(255) DEFAULT NULL,
                           `title` varchar(255) DEFAULT NULL
);

CREATE TABLE member (
                        `member_id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `created_at` datetime(6) DEFAULT NULL,
                        `modified_at` datetime(6) DEFAULT NULL,
                        `email` varchar(255) DEFAULT NULL,
                        `nickname` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL
);

CREATE TABLE heart (
                         `heart_id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `archive_id` bigint(20) DEFAULT NULL,
                         `member_id` bigint(20) DEFAULT NULL,
                         FOREIGN KEY (`archive_id`) REFERENCES `archive` (`archive_id`),
                         FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

CREATE TABLE review (
                          `review_id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `created_at` datetime(6) DEFAULT NULL,
                          `modified_at` datetime(6) DEFAULT NULL,
                          `content` varchar(255) DEFAULT NULL,
                          `deleted_at` datetime(6) DEFAULT NULL,
                          `head` varchar(255) DEFAULT NULL,
                          `scrap_cnt` bigint(20) NOT NULL,
                          `archive_id` bigint(20) DEFAULT NULL,
                          `member_id` bigint(20) DEFAULT NULL,
                          FOREIGN KEY (`archive_id`) REFERENCES `archive` (`archive_id`),
                          FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);

CREATE TABLE scrap (
                         `scrap_id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         `member_id` bigint(20) DEFAULT NULL,
                         `review_id` bigint(20) DEFAULT NULL,
                         FOREIGN KEY (`review_id`) REFERENCES `review` (`review_id`),
                         FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
);