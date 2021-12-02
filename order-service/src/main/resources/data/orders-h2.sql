CREATE TABLE orders(
       order_id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
       product_id int NOT NULL,
       qty int NOT NULL,
       user_id VARCHAR(50) NOT NULL,
       order_time datetime,
       total_price int,
       payment_yn VARCHAR(50),
       delivery_yn  VARCHAR(50)

);


insert into orders(order_id, product_id, qty,user_id,order_time,total_price, payment_yn,delivery_yn) values(100001, 9999 ,5,'hoon7566',sysdate,350000,'N','N' );
insert into orders(order_id, product_id, qty,user_id,order_time,total_price, payment_yn,delivery_yn) values(100002, 1000 ,3,'hoon7566',sysdate,50000,'N','N' );