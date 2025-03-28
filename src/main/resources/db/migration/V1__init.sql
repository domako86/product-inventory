CREATE TABLE category (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     description VARCHAR(255),
     price DECIMAL(10, 2) NOT NULL,
     quantity INT NOT NULL,
     version INT,
     category_id BIGINT,
     FOREIGN KEY (category_id) REFERENCES category(id)
);

INSERT INTO category (name) VALUES ('Electronics'), ('Toys'), ('Watches');