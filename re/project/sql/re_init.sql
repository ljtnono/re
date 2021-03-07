use re;
SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE blog_article;
TRUNCATE TABLE blog_article_tag;
TRUNCATE TABLE blog_comment;
TRUNCATE TABLE blog_tag;
TRUNCATE TABLE blog_type;
TRUNCATE TABLE git_repository;
TRUNCATE TABLE message;

TRUNCATE TABLE rs_book;
TRUNCATE TABLE rs_image;
TRUNCATE TABLE rs_link;
TRUNCATE TABLE rs_music;
TRUNCATE TABLE skill;

DELETE FROM sys_config WHERE id > 9;
ALTER TABLE sys_config AUTO_INCREMENT = 10;

TRUNCATE TABLE sys_job;
TRUNCATE TABLE sys_log;
TRUNCATE TABLE sys_message;
DELETE FROM sys_role WHERE id > 3;
ALTER TABLE sys_role AUTO_INCREMENT = 4;

TRUNCATE TABLE sys_timeline;
TRUNCATE TABLE user_skill;

show binary logs;
RESET MASTER;

SET FOREIGN_KEY_CHECKS=1;

