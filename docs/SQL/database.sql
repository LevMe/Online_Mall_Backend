-- 创建数据库
CREATE
    DATABASE IF NOT EXISTS `online_mall` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 切换到该数据库
USE
    `online_mall`;

-- ----------------------------
-- 1. 用户表 (users)
-- 存储系统的用户信息
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID, 主键',
    `username`   VARCHAR(50)     NOT NULL UNIQUE COMMENT '用户名, 必须唯一',
    `password`   VARCHAR(255)    NOT NULL COMMENT '密码, 存储加密后的哈希值',
    `email`      VARCHAR(100)    NOT NULL UNIQUE COMMENT '电子邮箱, 必须唯一',
    `avatar_url` VARCHAR(255)    NULL     DEFAULT 'https://placehold.co/100x100/EEE/31343C?text=Avatar' COMMENT '用户头像URL',
    `created_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- ----------------------------
-- 2. 商品分类表 (product_categories)
-- 存储商品的分类信息
-- ----------------------------
DROP TABLE IF EXISTS `product_categories`;
CREATE TABLE `product_categories`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID, 主键',
    `name`       VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `parent_id`  INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父分类ID, 0表示顶级分类',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品分类表';

-- ----------------------------
-- 3. 商品表 (products)
-- 存储商品详细信息
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID, 主键',
    `name`           VARCHAR(255)    NOT NULL COMMENT '商品名称',
    `description`    TEXT            NULL COMMENT '商品描述',
    `price`          DECIMAL(10, 2)  NOT NULL COMMENT '商品价格',
    `stock`          INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '库存数量',
    `category_id`    INT UNSIGNED    NOT NULL COMMENT '所属分类ID',
    `main_image_url` VARCHAR(255)    NULL COMMENT '商品主图URL',
    `image_urls`     JSON            NULL COMMENT '商品图片URL列表 (JSON数组)',
    `specs`          JSON            NULL COMMENT '商品规格参数 (JSON对象)',
    `created_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_category_id` (`category_id`),
    FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品表';

-- ----------------------------
-- 4. 购物车项表 (cart_items)
-- 存储用户的购物车信息
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items`
(
    `id`         VARCHAR(50)     NOT NULL COMMENT '购物车项ID, 使用UUID或雪花算法生成, 主键',
    `user_id`    BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `quantity`   INT UNSIGNED    NOT NULL DEFAULT 1 COMMENT '商品数量',
    `created_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`), -- 同一个用户对同一个商品只能有一条购物车记录
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='购物车项表';


-- ----------------------------
-- 5. 用户行为追踪表 (user_behaviors)
-- 存储用户行为数据，用于推荐系统
-- ----------------------------
DROP TABLE IF EXISTS `user_behaviors`;
CREATE TABLE `user_behaviors`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '行为ID, 主键',
    `user_id`    BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `event_type` VARCHAR(20)     NOT NULL COMMENT '事件类型 (click, addToCart, purchase)',
    `timestamp`  DATETIME        NOT NULL COMMENT '事件发生时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_event` (`user_id`, `event_type`),
    INDEX `idx_product_event` (`product_id`, `event_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户行为追踪表';

-- ----------------------------
-- 插入一些初始数据用于测试
-- ----------------------------
-- 插入商品分类
INSERT INTO `product_categories` (`id`, `name`, `parent_id`)
VALUES (1, '电子产品', 0),
       (2, '图书音像', 0),
       (3, '家居生活', 0),
       (4, '手机', 1),
       (5, '电脑', 1);

-- 插入商品
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `main_image_url`, `image_urls`,
                        `specs`)
VALUES (101, '智能手机 Pro', '一款性能卓越的智能手机，搭载最新的SuperChip A18处理器，拥有12GB超大内存。', 4999.00, 99, 4,
        'https://placehold.co/600x400/5E5CE6/FFFFFF?text=Phone+Pro', '[
    "https://placehold.co/600x400/5E5CE6/FFFFFF?text=Image1",
    "https://placehold.co/600x400/5E5CE6/FFFFFF?text=Image2"
  ]', '{
    "CPU": "SuperChip A18",
    "RAM": "12GB",
    "屏幕尺寸": "6.7英寸"
  }'),
       (102, '轻薄笔记本 Air', '极致轻薄，长效续航，适合移动办公和学习。', 7999.00, 50, 5,
        'https://placehold.co/600x400/007AFF/FFFFFF?text=Laptop+Air', '[]', '{
         "CPU": "Intel Core i7",
         "RAM": "16GB",
         "硬盘": "512GB SSD"
       }'),
       (201, 'Java编程思想', '经典的Java入门与进阶书籍，深入讲解面向对象思想。', 108.00, 200, 2,
        'https://placehold.co/600x400/34C759/FFFFFF?text=Java+Book', '[]', '{
         "作者": "Bruce Eckel",
         "出版社": "机械工业出版社"
       }'),
       (301, '舒适人体工学椅', '为长时间工作的你设计，有效缓解腰部压力。', 1299.00, 30, 3,
        'https://placehold.co/600x400/FF9500/FFFFFF?text=Ergo+Chair', '[]', '{
         "材质": "网布",
         "功能": "可调节扶手、腰靠"
       }');

