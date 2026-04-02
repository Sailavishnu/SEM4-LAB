CREATE TABLE Customerss (
    customer_id NUMBER PRIMARY KEY,
    name VARCHAR2(20) NOT NULL,
    email VARCHAR2(20) UNIQUE NOT NULL,
    phone VARCHAR2(20),
    password VARCHAR2(20) NOT NULL,
    address VARCHAR2(20),
    created_date DATE DEFAULT SYSDATE
);

CREATE TABLE Productss (
    product_id NUMBER PRIMARY KEY,
    product_name VARCHAR2(20) NOT NULL,
    category VARCHAR2(20),
    price NUMBER(10, 2) NOT NULL,
    stock_quantity NUMBER DEFAULT 0,
    description VARCHAR2(20)
);

CREATE TABLE Cartss (
    cart_id NUMBER PRIMARY KEY,
    customer_id NUMBER,
    created_date DATE DEFAULT SYSDATE,
    FOREIGN KEY (customer_id) REFERENCES Customerss(customer_id)
);

CREATE TABLE CartItemss (
    cart_item_id NUMBER PRIMARY KEY,
    cart_id NUMBER,
    product_id NUMBER,
    quantity NUMBER,
    FOREIGN KEY (cart_id) REFERENCES Cartss(cart_id),
    FOREIGN KEY (product_id) REFERENCES Productss(product_id)
);

CREATE TABLE Orderss (
    order_id NUMBER PRIMARY KEY,
    customer_id NUMBER,
    order_date DATE DEFAULT SYSDATE,
    total_amount NUMBER(10, 2),
    order_status VARCHAR2(20) DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES Customerss(customer_id)
);

CREATE TABLE OrderItemss (
    order_item_id NUMBER PRIMARY KEY,
    order_id NUMBER,
    product_id NUMBER,
    quantity NUMBER,
    price NUMBER(10, 2),
    FOREIGN KEY (order_id) REFERENCES Orderss(order_id),
    FOREIGN KEY (product_id) REFERENCES Productss(product_id)
);

-- Create sequences for primary keys
CREATE SEQUENCE customerss_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE productss_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE cartss_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE cartitemss_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE orderss_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE orderitemss_seq START WITH 1 INCREMENT BY 1;
desc CartItems;
desc Carts;
desc Products;
desc Customers;

