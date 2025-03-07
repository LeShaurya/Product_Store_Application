CREATE TABLE IF NOT EXISTS `Orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `sku_code` VARCHAR(255) NOT NULL,
    `quantity` INT NOT NULL CHECK (quantity > 0),
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `customer_name` VARCHAR(100) NOT NULL,
    `customer_email` VARCHAR(100),
    `customer_phone` VARCHAR(15),
    `shipping_address` TEXT
);
