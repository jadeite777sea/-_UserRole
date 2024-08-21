INSERT INTO user (id, user_name, password, avatar_path, phone, email, gender, address, introduction, true_name, create_time, update_time, status)
VALUES
    (1, 'JohnDoe', 'password123', '/avatars/johndoe.png', '1234567890', 'johndoe@example.com', 1, '123 Main St', 'Hello, I am John.', 'John Doe', NOW(), NOW(), 1),
    (2, 'JaneSmith', 'password123', '/avatars/janesmith.png', '0987654321', 'janesmith@example.com', 2, '456 Oak St', 'Hello, I am Jane.', 'Jane Smith', NOW(), NOW(), 1),
    (3, 'AliceJohnson', 'password123', '/avatars/alicejohnson.png', '1122334455', 'alicejohnson@example.com', 2, '789 Pine St', 'Hello, I am Alice.', 'Alice Johnson', NOW(), NOW(), 1),
    (4, 'BobBrown', 'password123', '/avatars/bobbrown.png', '6677889900', 'bobbrown@example.com', 1, '101 Maple St', 'Hello, I am Bob.', 'Bob Brown', NOW(), NOW(), 1);


INSERT INTO user_role (id, user_id, role_id, create_time)
VALUES
    (1, 1, 2, NOW()),  -- JohnDoe 拥有 role_id 为 2 的角色
    (2, 2, 3, NOW()),  -- JaneSmith 拥有 role_id 为 3 的角色
    (3, 3, 2, NOW()),  -- AliceJohnson 拥有 role_id 为 2 的角色
    (4, 4, 3, NOW()),  -- BobBrown 拥有 role_id 为 3 的角色
    (5, 4, 2, NOW());  -- BobBrown 也拥有 role_id 为 2 的角色
