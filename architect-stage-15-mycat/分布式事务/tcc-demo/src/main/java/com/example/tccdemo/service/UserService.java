package com.example.tccdemo.service;

import com.example.tccdemo.db131.dao.UserMapper;
import com.example.tccdemo.db131.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private CuratorFramework zkClient;


    public List<User> getAllUsers() {

        return userMapper.selectAllUsers();
    }

    public int delUser(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user!=null){
            log.info("用户存在，用户为："+userId);
            return userMapper.deleteByPrimaryKey(userId);
        }
        log.info("用户不存在存在，用户为："+userId);
        return 0;
    }

    public User selectById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public int insertUser(User user, String token) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(zkClient, "/"+token);
        boolean isLock = lock.acquire(30, TimeUnit.SECONDS);
        if (isLock){
            return userMapper.insertSelective(user);




        }
        return 0;
    }
}
