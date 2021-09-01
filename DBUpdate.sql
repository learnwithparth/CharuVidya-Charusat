DROP TABLE IF EXISTS `course_review_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_review_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `status_updated_on` date DEFAULT NULL,
  `feedback` varchar(255) DEFAULT NULL,
  `reviewer_id` bigint DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_course_review_status__user_id` (`reviewer_id`),
  KEY `fk_course_review_status___course_id` (`course_id`),
  CONSTRAINT `fk_course_review_status___course_id` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_course_review_status__user_id` FOREIGN KEY (`reviewer_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `rel_course_category__reviewers_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rel_course_category__reviewers_list` (
  `course_category_id` bigint NOT NULL,
  `reviewer_id` bigint NOT NULL,
  PRIMARY KEY (`course_category_id`,`reviewer_id`),
  KEY `fk_rel_course_category__reviewers_list__reviewer_id` (`reviewer_id`),
  CONSTRAINT `fk_rel_course_category__reviewers_list__reviewer_id` FOREIGN KEY (`reviewer_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE `codehat_charusat`.`course`
ADD COLUMN `course_review_status_id` BIGINT NULL DEFAULT NULL AFTER `reviewer_id`,
ADD CONSTRAINT `fk_course__course_review_status_id`
FOREIGN KEY (`course_review_status_id`)
REFERENCES `codehat_charusat`.`course_review_status` (`id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
