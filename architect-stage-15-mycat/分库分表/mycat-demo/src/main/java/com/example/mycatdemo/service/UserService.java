package com.example.mycatdemo.service;

import com.example.mycatdemo.dao.UserMapper;
import com.example.mycatdemo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void testUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("奇数");
        userMapper.insert(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("偶数111");
        userMapper.insert(user2);
    }

}
