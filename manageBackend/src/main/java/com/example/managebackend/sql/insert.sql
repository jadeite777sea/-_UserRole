

INSERT INTO `user`(`id`, `user_name`, `password`, `status`) VALUES
(4, 'user4', 'e10adc3949ba59abbe56e057f20f883e', 1),
(5, 'user5', 'e10adc3949ba59abbe56e057f20f883e', 1),
(6, 'user6', 'e10adc3949ba59abbe56e057f20f883e', 1),
(7, 'user7', 'e10adc3949ba59abbe56e057f20f883e', 1),
(8, 'user8', 'e10adc3949ba59abbe56e057f20f883e', 1),
(9, 'user9', 'e10adc3949ba59abbe56e057f20f883e', 1),
(10, 'user10', 'e10adc3949ba59abbe56e057f20f883e', 1),
(11, 'user11', 'e10adc3949ba59abbe56e057f20f883e', 1),
(12, 'user12', 'e10adc3949ba59abbe56e057f20f883e', 1),
(13, 'user13', 'e10adc3949ba59abbe56e057f20f883e', 1),
(14, 'user14', 'e10adc3949ba59abbe56e057f20f883e', 1),
(15, 'user15', 'e10adc3949ba59abbe56e057f20f883e', 1),
(16, 'user16', 'e10adc3949ba59abbe56e057f20f883e', 1),
(17, 'user17', 'e10adc3949ba59abbe56e057f20f883e', 1),
(18, 'user18', 'e10adc3949ba59abbe56e057f20f883e', 1),
(19, 'user19', 'e10adc3949ba59abbe56e057f20f883e', 1),
(20, 'user20', 'e10adc3949ba59abbe56e057f20f883e', 1),
(21, 'user21', 'e10adc3949ba59abbe56e057f20f883e', 1),
(22, 'user22', 'e10adc3949ba59abbe56e057f20f883e', 1),
(23, 'user23', 'e10adc3949ba59abbe56e057f20f883e', 1),
(24, 'user24', 'e10adc3949ba59abbe56e057f20f883e', 1),
(25, 'user25', 'e10adc3949ba59abbe56e057f20f883e', 1),
(26, 'user26', 'e10adc3949ba59abbe56e057f20f883e', 1),
(27, 'user27', 'e10adc3949ba59abbe56e057f20f883e', 1),
(28, 'user28', 'e10adc3949ba59abbe56e057f20f883e', 1),
(29, 'user29', 'e10adc3949ba59abbe56e057f20f883e', 1),
(30, 'user30', 'e10adc3949ba59abbe56e057f20f883e', 1);


-- 为 user4 到 user30 设置角色为普通用户 (role_id = 2)
INSERT INTO `user_role`(`user_id`, `role_id`, `create_time`)
VALUES
    (4, 2, CURRENT_TIMESTAMP),
    (5, 2, CURRENT_TIMESTAMP),
    (6, 2, CURRENT_TIMESTAMP),
    (7, 2, CURRENT_TIMESTAMP),
    (8, 2, CURRENT_TIMESTAMP),
    (9, 2, CURRENT_TIMESTAMP),
    (10, 2, CURRENT_TIMESTAMP),
    (11, 2, CURRENT_TIMESTAMP),
    (12, 2, CURRENT_TIMESTAMP),
    (13, 2, CURRENT_TIMESTAMP),
    (14, 2, CURRENT_TIMESTAMP),
    (15, 2, CURRENT_TIMESTAMP),
    (16, 2, CURRENT_TIMESTAMP),
    (17, 2, CURRENT_TIMESTAMP),
    (18, 2, CURRENT_TIMESTAMP),
    (19, 2, CURRENT_TIMESTAMP),
    (20, 2, CURRENT_TIMESTAMP),
    (21, 2, CURRENT_TIMESTAMP),
    (22, 2, CURRENT_TIMESTAMP),
    (23, 2, CURRENT_TIMESTAMP),
    (24, 2, CURRENT_TIMESTAMP),
    (25, 2, CURRENT_TIMESTAMP),
    (26, 2, CURRENT_TIMESTAMP),
    (27, 2, CURRENT_TIMESTAMP),
    (28, 2, CURRENT_TIMESTAMP),
    (29, 2, CURRENT_TIMESTAMP),
    (30, 2, CURRENT_TIMESTAMP);
