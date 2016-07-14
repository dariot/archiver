CREATE TABLE "APP"."CATEGORY"
(
   ID int PRIMARY KEY NOT NULL,
   NAME varchar(200)
)
;
CREATE TABLE "APP"."DOCUMENT"
(
   ID int PRIMARY KEY NOT NULL,
   ENTITY_ID int,
   DATA clob(1073741823)
)
;
CREATE TABLE "APP"."ENTITY"
(
   ID int PRIMARY KEY NOT NULL,
   CATEGORY_ID int,
   AUTHOR varchar(200),
   TITLE varchar(500),
   TECHNIQUE varchar(1000),
   MEASURES varchar(200),
   BUY_YEAR varchar(100),
   PRICE varchar(100),
   PAYMENT_TYPE varchar(200),
   ORIGINAL_PLACE varchar(500),
   ACTUAL_PLACE varchar(500),
   CURRENT_VALUE varchar(200),
   CURRENT_VALUE_DATE varchar(200),
   NOTES varchar(1000),
   SOLD varchar(400)
)
;
CREATE TABLE "APP"."PICTURE"
(
   ID int PRIMARY KEY NOT NULL,
   ENTITY_ID int NOT NULL,
   DATA clob(1073741823),
   IS_MAIN_PIC varchar(1)
)
;
