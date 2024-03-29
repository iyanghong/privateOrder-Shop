# 点餐系统
-- 商品分类表
CREATE TABLE IF NOT EXISTS product_category
(
    id          varchar(36)  NOT NULL COMMENT '商品分类id',
    name        varchar(50)  NOT NULL COMMENT '商品分类名称',
    description varchar(100) NULL COMMENT '商品分类描述',
    parent_id   varchar(36)  NOT NULL DEFAULT '-1' COMMENT '父级分类id',
    sort_no     int          NOT NULL DEFAULT 0 COMMENT '排序号',
    creator     varchar(36)  NOT NULL COMMENT '创建者id',
    created_at  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX PARENT_IDX (parent_id ASC) USING BTREE COMMENT '父级分类id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '商品分类'
  ROW_FORMAT = DYNAMIC;


-- 商品表
CREATE TABLE IF NOT EXISTS product
(
    id            varchar(36)    NOT NULL COMMENT '商品id',
    name          varchar(50)    NOT NULL COMMENT '商品名称',
    description   text           NULL COMMENT '商品描述',
    price         decimal(10, 2) NOT NULL COMMENT '商品价格',
    img_url       text           NULL     default '[]' COMMENT '商品图片地址',
    specification text           NULL COMMENT '商品规格',
    stock         int            NOT NULL DEFAULT 0 COMMENT '商品库存',
    status        tinyint        NOT NULL DEFAULT 0 COMMENT '商品状态:0-下架,1-上架',
    release_time  datetime       NULL COMMENT '上架时间',
    category_id   varchar(36)    NOT NULL COMMENT '商品分类id',
    creator       varchar(36)    NOT NULL COMMENT '创建者id',
    created_at    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX CATEGORY_IDX (category_id ASC) USING BTREE COMMENT '商品分类id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '商品'
  ROW_FORMAT = DYNAMIC;

-- 购物车表
CREATE TABLE IF NOT EXISTS cart
(
    id             varchar(36) NOT NULL COMMENT '购物车id',
    user_id        varchar(36) NOT NULL COMMENT '用户id',
    product_id     varchar(36) NOT NULL COMMENT '商品id',
    selected_param text        NULL COMMENT '已选规格',
    quantity       int         NOT NULL COMMENT '商品数量',
    created_at     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX USER_IDX (user_id ASC) USING BTREE COMMENT '用户id索引',
    INDEX PRODUCT_IDX (product_id ASC) USING BTREE COMMENT '商品id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '购物车'
  ROW_FORMAT = DYNAMIC;

-- 订单表
CREATE TABLE IF NOT EXISTS orders
(
    id          varchar(36)    NOT NULL COMMENT '订单id',
    user_id     varchar(36)    NOT NULL COMMENT '用户id',
    address     varchar(36)    NULL COMMENT '收货地址id',
    total_price decimal(10, 2) NOT NULL COMMENT '订单总价',
    status      tinyint        NOT NULL DEFAULT 0 COMMENT '订单状态:0-未支付,1-已支付,2-已发货,3-已收货,4-已取消',
    created_at  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX USER_IDX (user_id ASC) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '订单'
  ROW_FORMAT = DYNAMIC;

-- 订单商品表
CREATE TABLE IF NOT EXISTS order_product
(
    id             varchar(36)    NOT NULL COMMENT '订单商品id',
    order_id       varchar(36)    NOT NULL COMMENT '订单id',
    product_id     varchar(36)    NOT NULL COMMENT '商品id',
    quantity       int            NOT NULL COMMENT '商品数量',
    selected_param text           NULL COMMENT '已选规格',
    price          decimal(10, 2) NOT NULL COMMENT '商品价格',
    created_at     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX ORDER_IDX (order_id ASC) USING BTREE COMMENT '订单id索引',
    INDEX PRODUCT_IDX (product_id ASC) USING BTREE COMMENT '商品id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '订单商品'
  ROW_FORMAT = DYNAMIC;


-- 好友表
CREATE TABLE IF NOT EXISTS friend
(
    id         varchar(36) NOT NULL COMMENT '好友id',
    user_id    varchar(36) NOT NULL COMMENT '用户id',
    friend_id  varchar(36) NOT NULL COMMENT '好友id',
    remark     varchar(50) NULL COMMENT '备注',
    created_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX USER_IDX (user_id ASC) USING BTREE COMMENT '用户id索引',
    INDEX FRIEND_IDX (friend_id ASC) USING BTREE COMMENT '好友id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '好友'
  ROW_FORMAT = DYNAMIC;

-- 好友申请表
CREATE TABLE IF NOT EXISTS friend_request
(
    id         varchar(36)  NOT NULL COMMENT '好友申请id',
    user_id    varchar(36)  NOT NULL COMMENT '用户id',
    friend_id  varchar(36)  NOT NULL COMMENT '好友id',
    message    varchar(255) NULL COMMENT '好友申请消息',
    status     tinyint      NOT NULL COMMENT '好友申请状态:0-未处理,1-已同意,2-已拒绝',
    created_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX USER_IDX (user_id ASC) USING BTREE COMMENT '用户id索引',
    INDEX FRIEND_IDX (friend_id ASC) USING BTREE COMMENT '好友id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '好友申请'
  ROW_FORMAT = DYNAMIC;

-- 收货地址
CREATE TABLE IF NOT EXISTS addresses
(
    id             varchar(36)  NOT NULL COMMENT '收货地址id',
    user_id        varchar(36)  NOT NULL COMMENT '用户id',
    name           varchar(50)  NOT NULL COMMENT '收货人姓名',
    phone          varchar(20)  NOT NULL COMMENT '收货人电话',
    address        varchar(255) NOT NULL COMMENT '收货地址',
    address_detail varchar(255) NULL COMMENT '详细地址',
    is_default     tinyint      NOT NULL COMMENT '是否默认地址:0-否,1-是',
    created_at     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX USER_IDX (user_id ASC) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '收货地址'
  ROW_FORMAT = DYNAMIC;

-- 用户表
CREATE TABLE IF NOT EXISTS user
(
    id              varchar(36)    NOT NULL COMMENT '用户id',
    username        varchar(50)    NOT NULL COMMENT '用户名',
    password        varchar(50)    NOT NULL COMMENT '密码',
    nickname        varchar(50)    NOT NULL COMMENT '昵称',
    gender          TINYINT        NOT NULL DEFAULT 0 COMMENT '性别：0-未知,1-男,2-女',
    avatar          varchar(255)   NULL COMMENT '头像',
    type            tinyint        NOT NULL COMMENT '用户类型:0-普通用户,1-商家',
    phone           varchar(20)    NULL COMMENT '手机号',
    email           varchar(50)    NULL COMMENT '邮箱',
    money           decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
    login_ip        VARCHAR(64)    NULL     DEFAULT NULL COMMENT '登录ip',
    last_login_time datetime       NULL     DEFAULT NULL COMMENT '最后登陆时间',
    created_at      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE INDEX USERNAME_UNIQUE (username ASC) USING BTREE COMMENT '用户名唯一索引',
    UNIQUE INDEX PHONE_UNIQUE (phone ASC) USING BTREE COMMENT '手机号唯一索引',
    UNIQUE INDEX EMAIL_UNIQUE (email ASC) USING BTREE COMMENT '邮箱唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户'
  ROW_FORMAT = DYNAMIC;