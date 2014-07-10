/*
Navicat SQLite Data Transfer

Source Server         : myleitnerAndroid
Source Server Version : 30714
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30714
File Encoding         : 65001

Date: 2014-07-10 13:31:25
*/

PRAGMA foreign_keys = OFF;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS "main"."user";
CREATE TABLE "user" (
"USER_ID"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
"FIRST_NAME"  TEXT,
"LAST_NAME"  TEXT,
"DISPLAY_NAME"  TEXT,
"EMAIL_ADDRESS"  TEXT,
"PICTURE"  TEXT
);
