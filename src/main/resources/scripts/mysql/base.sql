SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sec_audit_type`
-- ----------------------------
DROP TABLE IF EXISTS `sec_audit_type`;
CREATE TABLE `sec_audit_type` (
  `audit_type_id` int(11) NOT NULL,
  `audit_type_name` varchar(255) NOT NULL,
  `module_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`audit_type_id`),
  KEY `module_id` (`module_id`) USING BTREE,
  CONSTRAINT `sec_audit_type_ibfk_1` FOREIGN KEY (`module_id`) REFERENCES `sec_modules` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 25600 kB; (`module_id`) REFER `asu-22-1-2012/sec_modules`(`module_i';

-- ----------------------------
-- Records of sec_audit_type
-- ----------------------------
INSERT INTO `sec_audit_type` VALUES ('1', 'Add record', null);
INSERT INTO `sec_audit_type` VALUES ('2', 'Update Record', null);
INSERT INTO `sec_audit_type` VALUES ('3', 'Delete Record', null);
INSERT INTO `sec_audit_type` VALUES ('4', 'User Log In', null);
INSERT INTO `sec_audit_type` VALUES ('5', 'User Logout', null);

-- ----------------------------
-- Table structure for `sec_audits`
-- ----------------------------
DROP TABLE IF EXISTS `sec_audits`;
CREATE TABLE `sec_audits` (
  `audit_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_record_id` int(11) NOT NULL,
  `audit_type_id` int(11) NOT NULL,
  `record_id` int(11) DEFAULT NULL,
  `record_name` text,
  `old_value` longtext,
  `new_value` longtext,
  `description` longtext,
  `gui` longtext,
  `audit_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`audit_id`),
  KEY `audit_type_id` (`audit_type_id`) USING BTREE,
  KEY `user_id` (`user_record_id`) USING BTREE,
  CONSTRAINT `sec_audits_ibfk_1` FOREIGN KEY (`audit_type_id`) REFERENCES `sec_audit_type` (`audit_type_id`) ON UPDATE CASCADE,
  CONSTRAINT `sec_audits_ibfk_2` FOREIGN KEY (`user_record_id`) REFERENCES `sec_users` (`user_record_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 17408 kB; (`audit_type_id`) REFER `1_university';

-- ----------------------------
-- Records of sec_audits
-- ----------------------------

-- ----------------------------
-- Table structure for `sec_modules`
-- ----------------------------
DROP TABLE IF EXISTS `sec_modules`;
CREATE TABLE `sec_modules` (
  `module_id` int(11) NOT NULL,
  `module_name` varchar(255) NOT NULL,
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `sec_privileges`
-- ----------------------------
DROP TABLE IF EXISTS `sec_privileges`;
CREATE TABLE `sec_privileges` (
  `privilege_id` int(11) NOT NULL,
  `privilege_name` varchar(255) NOT NULL,
  `privilege_desc` varchar(255) NOT NULL,
  `parent_privilege` int(11) DEFAULT NULL,
  `priv_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `sec_privilige_config`
-- ----------------------------
DROP TABLE IF EXISTS `sec_privilige_config`;
CREATE TABLE `sec_privilige_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reg_head_privilige_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_privilige_config
-- ----------------------------

-- ----------------------------
-- Table structure for `sec_role_privileges`
-- ----------------------------
DROP TABLE IF EXISTS `sec_role_privileges`;
CREATE TABLE `sec_role_privileges` (
  `role_privilege_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `privilege_id` int(11) NOT NULL,
  PRIMARY KEY (`role_privilege_id`),
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `privilege_id` (`privilege_id`) USING BTREE,
  CONSTRAINT `sec_role_privileges_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sec_roles` (`role_id`) ON UPDATE CASCADE,
  CONSTRAINT `sec_role_privileges_ibfk_2` FOREIGN KEY (`privilege_id`) REFERENCES `sec_privileges` (`privilege_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 17408 kB; (`role_id`) REFER `1_university/sec_r';

-- ----------------------------
-- Records of sec_role_privileges
-- ----------------------------

-- ----------------------------
-- Table structure for `sec_roles`
-- ----------------------------
DROP TABLE IF EXISTS `sec_roles`;
CREATE TABLE `sec_roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_roles
-- ----------------------------
INSERT INTO `sec_roles` VALUES ('1', 'System Administrator');

-- ----------------------------
-- Table structure for `sec_user_roles`
-- ----------------------------
DROP TABLE IF EXISTS `sec_user_roles`;
CREATE TABLE `sec_user_roles` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `sec_user_roles_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sec_roles` (`role_id`) ON UPDATE CASCADE,
  CONSTRAINT `sec_user_roles_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`user_record_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 17408 kB; (`role_id`) REFER `1_university/sec_r';

-- ----------------------------
-- Records of sec_user_roles
-- ----------------------------

-- ----------------------------
-- Table structure for `sec_users`
-- ----------------------------
DROP TABLE IF EXISTS `sec_users`;
CREATE TABLE `sec_users` (
  `user_record_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `user_full_name` varchar(255) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `disabled` int(11) NOT NULL,
  PRIMARY KEY (`user_record_id`),
  UNIQUE KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_users
-- ----------------------------
INSERT INTO `sec_users` VALUES ('1', 'admin', 'Administator', 'MTIzNDU2', '0');

CREATE TABLE `version` (
  `version_id` int(10) NOT NULL AUTO_INCREMENT,
  `version_number` varchar(50) DEFAULT NULL,
  `version_date` date DEFAULT NULL,
  `version_note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;