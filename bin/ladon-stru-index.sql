-- ----------------------------
-- Primary Key structure for table gorp_migrations
-- ----------------------------
ALTER TABLE "public"."gorp_migrations" ADD CONSTRAINT "gorp_migrations_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ladon_action
-- ----------------------------
CREATE INDEX "ladon_permission_compiled_idx" ON "public"."ladon_action" USING btree (
  "compiled" COLLATE "pg_catalog"."default" "pg_catalog"."text_pattern_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table ladon_action
-- ----------------------------
ALTER TABLE "public"."ladon_action" ADD CONSTRAINT "ladon_action_compiled_key" UNIQUE ("compiled");
ALTER TABLE "public"."ladon_action" ADD CONSTRAINT "ladon_action_template_key" UNIQUE ("template");

-- ----------------------------
-- Primary Key structure for table ladon_action
-- ----------------------------
ALTER TABLE "public"."ladon_action" ADD CONSTRAINT "ladon_action_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Checks structure for table ladon_policy
-- ----------------------------
ALTER TABLE "public"."ladon_policy" ADD CONSTRAINT "ladon_policy_effect_check" CHECK (((effect = 'allow'::text) OR (effect = 'deny'::text)));

-- ----------------------------
-- Primary Key structure for table ladon_policy
-- ----------------------------
ALTER TABLE "public"."ladon_policy" ADD CONSTRAINT "ladon_policy_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ladon_policy_action_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_action_rel" ADD CONSTRAINT "ladon_policy_action_rel_pkey" PRIMARY KEY ("policy", "action");

-- ----------------------------
-- Primary Key structure for table ladon_policy_resource_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_resource_rel" ADD CONSTRAINT "ladon_policy_resource_rel_pkey" PRIMARY KEY ("policy", "resource");

-- ----------------------------
-- Primary Key structure for table ladon_policy_subject_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_subject_rel" ADD CONSTRAINT "ladon_policy_subject_rel_pkey" PRIMARY KEY ("policy", "subject");

-- ----------------------------
-- Indexes structure for table ladon_resource
-- ----------------------------
CREATE INDEX "ladon_resource_compiled_idx" ON "public"."ladon_resource" USING btree (
  "compiled" COLLATE "pg_catalog"."default" "pg_catalog"."text_pattern_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table ladon_resource
-- ----------------------------
ALTER TABLE "public"."ladon_resource" ADD CONSTRAINT "ladon_resource_compiled_key" UNIQUE ("compiled");
ALTER TABLE "public"."ladon_resource" ADD CONSTRAINT "ladon_resource_template_key" UNIQUE ("template");

-- ----------------------------
-- Primary Key structure for table ladon_resource
-- ----------------------------
ALTER TABLE "public"."ladon_resource" ADD CONSTRAINT "ladon_resource_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ladon_subject
-- ----------------------------
CREATE INDEX "ladon_subject_compiled_idx" ON "public"."ladon_subject" USING btree (
  "compiled" COLLATE "pg_catalog"."default" "pg_catalog"."text_pattern_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table ladon_subject
-- ----------------------------
ALTER TABLE "public"."ladon_subject" ADD CONSTRAINT "ladon_subject_compiled_key" UNIQUE ("compiled");
ALTER TABLE "public"."ladon_subject" ADD CONSTRAINT "ladon_subject_template_key" UNIQUE ("template");

-- ----------------------------
-- Primary Key structure for table ladon_subject
-- ----------------------------
ALTER TABLE "public"."ladon_subject" ADD CONSTRAINT "ladon_subject_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_action_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_action_rel" ADD CONSTRAINT "ladon_policy_action_rel_action_fkey" FOREIGN KEY ("action") REFERENCES "public"."ladon_action" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "public"."ladon_policy_action_rel" ADD CONSTRAINT "ladon_policy_action_rel_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_permission
-- ----------------------------
ALTER TABLE "public"."ladon_policy_permission" ADD CONSTRAINT "ladon_policy_permission_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_resource
-- ----------------------------
ALTER TABLE "public"."ladon_policy_resource" ADD CONSTRAINT "ladon_policy_resource_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_resource_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_resource_rel" ADD CONSTRAINT "ladon_policy_resource_rel_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "public"."ladon_policy_resource_rel" ADD CONSTRAINT "ladon_policy_resource_rel_resource_fkey" FOREIGN KEY ("resource") REFERENCES "public"."ladon_resource" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_subject
-- ----------------------------
ALTER TABLE "public"."ladon_policy_subject" ADD CONSTRAINT "ladon_policy_subject_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table ladon_policy_subject_rel
-- ----------------------------
ALTER TABLE "public"."ladon_policy_subject_rel" ADD CONSTRAINT "ladon_policy_subject_rel_policy_fkey" FOREIGN KEY ("policy") REFERENCES "public"."ladon_policy" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "public"."ladon_policy_subject_rel" ADD CONSTRAINT "ladon_policy_subject_rel_subject_fkey" FOREIGN KEY ("subject") REFERENCES "public"."ladon_subject" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
