DROP TABLE IF EXISTS "public"."user";
DROP TABLE IF EXISTS "public"."post";
DROP TABLE IF EXISTS "public"."post_reply";

CREATE TABLE IF NOT EXISTS "public"."user"
(
    "id"   int8 NOT NULL GENERATED ALWAYS AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
),
    "name" varchar(30) COLLATE "pg_catalog"."default",
    CONSTRAINT "user_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."user"
    OWNER TO "root";

COMMENT
ON COLUMN "public"."user"."id" IS 'ID';

COMMENT
ON COLUMN "public"."user"."name" IS '名稱';

CREATE TABLE IF NOT EXISTS "public"."post"
(
    "user_id"     int8 NOT NULL,
    "time"        int8 NOT NULL,
    "message"     varchar(255) COLLATE "pg_catalog"."default",
    "reply_count" int4,
    CONSTRAINT "post_pkey" PRIMARY KEY ("user_id", "time")
)
;
ALTER TABLE "public"."post"
    OWNER TO "root";

CREATE TABLE IF NOT EXISTS "public"."post_reply"
(
    "user_id"      int8                                        NOT NULL,
    "time"         int8                                        NOT NULL,
    "post_user_id" int8                                        NOT NULL,
    "post_time"    int8                                        NOT NULL,
    "message"      varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "reply_count"  int4                                        NOT NULL,
    CONSTRAINT "post_reply_pkey" PRIMARY KEY ("user_id", "time", "post_user_id", "post_time", "message", "reply_count")
)
;

ALTER TABLE "public"."post_reply"
    OWNER TO "root";
