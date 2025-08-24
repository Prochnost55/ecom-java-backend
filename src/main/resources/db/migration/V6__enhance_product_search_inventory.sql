-- Enhanced Product Search and Inventory Features
-- This migration adds inventory management and search enhancement fields

-- Add inventory and search fields to product table
ALTER TABLE product ADD COLUMN IF NOT EXISTS stock_quantity INT DEFAULT 0;
ALTER TABLE product ADD COLUMN IF NOT EXISTS in_stock BOOLEAN DEFAULT TRUE;
ALTER TABLE product ADD COLUMN IF NOT EXISTS brand VARCHAR(255);
ALTER TABLE product ADD COLUMN IF NOT EXISTS search_keywords VARCHAR(1000);
ALTER TABLE product ADD COLUMN IF NOT EXISTS status ENUM('ACTIVE', 'INACTIVE', 'DRAFT', 'DISCONTINUED') DEFAULT 'ACTIVE';

-- Update description column to allow longer text
ALTER TABLE product MODIFY COLUMN description TEXT;

-- Create product tags table for ElementCollection
CREATE TABLE IF NOT EXISTS product_tags (
    product_id BINARY(16) NOT NULL,
    tag VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    INDEX idx_product_tags_product_id (product_id),
    INDEX idx_product_tags_tag (tag)
);

-- Create indexes for better search performance
CREATE INDEX IF NOT EXISTS idx_product_title ON product(title);
CREATE INDEX IF NOT EXISTS idx_product_brand ON product(brand);
CREATE INDEX IF NOT EXISTS idx_product_status ON product(status);
CREATE INDEX IF NOT EXISTS idx_product_stock ON product(stock_quantity);
CREATE INDEX IF NOT EXISTS idx_product_in_stock ON product(in_stock);
CREATE INDEX IF NOT EXISTS idx_product_search_keywords ON product(search_keywords);

-- Create full-text index for advanced search (MySQL specific)
-- ALTER TABLE product ADD FULLTEXT(title, description, search_keywords);

-- Update existing products with default inventory values
UPDATE product SET 
    stock_quantity = 100, 
    in_stock = TRUE, 
    status = 'ACTIVE'
WHERE stock_quantity IS NULL OR stock_quantity = 0;

-- Add some sample search keywords based on existing data
UPDATE product SET 
    search_keywords = CONCAT(COALESCE(title, ''), ' ', COALESCE(description, ''), ' ', COALESCE(brand, ''))
WHERE search_keywords IS NULL;
