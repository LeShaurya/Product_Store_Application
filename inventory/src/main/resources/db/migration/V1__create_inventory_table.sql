CREATE TABLE `inventory` (
    `sku_code` VARCHAR(255) NOT NULL PRIMARY KEY,
    `quantity` INT NOT NULL CHECK (quantity > 0)
);
