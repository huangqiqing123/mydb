/*
 Navicat Premium Data Transfer

 Source Server         : localhost-postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 100012
 Source Host           : localhost:5432
 Source Catalog        : ladon
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100012
 File Encoding         : 65001

 Date: 25/04/2021 17:42:54
*/


-- ----------------------------
-- Table structure for gorp_migrations
-- ----------------------------
DROP TABLE IF EXISTS "public"."gorp_migrations";
CREATE TABLE "public"."gorp_migrations" (
  "id" text COLLATE "pg_catalog"."default" NOT NULL,
  "applied_at" timestamptz(6)
)
;
-- ----------------------------
-- Table structure for ladon_action
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_action";
CREATE TABLE "public"."ladon_action" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "has_regex" bool NOT NULL,
  "compiled" varchar(511) COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(511) COLLATE "pg_catalog"."default" NOT NULL
)
;
-- ----------------------------
-- Table structure for ladon_policy
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy";
CREATE TABLE "public"."ladon_policy" (
  "id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default" NOT NULL,
  "effect" text COLLATE "pg_catalog"."default" NOT NULL,
  "conditions" text COLLATE "pg_catalog"."default" NOT NULL,
  "meta" json
)
;
-- ----------------------------
-- Table structure for ladon_policy_action_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_action_rel";
CREATE TABLE "public"."ladon_policy_action_rel" (
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "action" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_policy_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_permission";
CREATE TABLE "public"."ladon_policy_permission" (
  "compiled" text COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(1023) COLLATE "pg_catalog"."default" NOT NULL,
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_policy_resource
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_resource";
CREATE TABLE "public"."ladon_policy_resource" (
  "compiled" text COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(1023) COLLATE "pg_catalog"."default" NOT NULL,
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_policy_resource_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_resource_rel";
CREATE TABLE "public"."ladon_policy_resource_rel" (
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "resource" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_policy_subject
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_subject";
CREATE TABLE "public"."ladon_policy_subject" (
  "compiled" text COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(1023) COLLATE "pg_catalog"."default" NOT NULL,
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_policy_subject_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_policy_subject_rel";
CREATE TABLE "public"."ladon_policy_subject_rel" (
  "policy" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "subject" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Table structure for ladon_resource
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_resource";
CREATE TABLE "public"."ladon_resource" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "has_regex" bool NOT NULL,
  "compiled" varchar(511) COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(511) COLLATE "pg_catalog"."default" NOT NULL
)
;
-- ----------------------------
-- Table structure for ladon_subject
-- ----------------------------
DROP TABLE IF EXISTS "public"."ladon_subject";
CREATE TABLE "public"."ladon_subject" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "has_regex" bool NOT NULL,
  "compiled" varchar(511) COLLATE "pg_catalog"."default" NOT NULL,
  "template" varchar(511) COLLATE "pg_catalog"."default" NOT NULL
)
;
