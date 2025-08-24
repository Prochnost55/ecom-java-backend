-- Add cart tables to the database
-- This migration creates the cart and cart_item tables for the e-commerce system

-- Create carts table
CREATE TABLE IF NOT EXISTS carts (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    status ENUM('ACTIVE', 'CHECKED_OUT', 'ABANDONED') NOT NULL DEFAULT 'ACTIVE',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cart_user_id (user_id),
    INDEX idx_cart_status (status)
);

-- Create cart_items table
CREATE TABLE IF NOT EXISTS cart_items (
    id BINARY(16) PRIMARY KEY,
    cart_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_cart_item_cart_id (cart_id),
    INDEX idx_cart_item_product_id (product_id),
    UNIQUE KEY uk_cart_product (cart_id, product_id)
);
