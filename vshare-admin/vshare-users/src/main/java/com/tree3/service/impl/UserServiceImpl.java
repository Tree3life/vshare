package com.tree3.service.impl;

import com.tree3.dao.UserMapper;
import com.tree3.pojo.entity.User;
import com.tree3.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author rupert
 * @since 2024-03-10 09:32:25
 */
@Slf4j
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }


    //SUPPORTS 支持事务： 外层存在事务融入当前事务  外层不存在事务 不会开启新的事务
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> queryAll(User user) {
        return userMapper.queryAll(user);
    }

    @Override
    public User queryUser(User user) {
        return userMapper.queryUser(user);
    }


    @Override
    public User queryById(Integer id) {
        return userMapper.queryById(id);
    }

    @Override
    public User update(User user) {
        this.userMapper.update(user);
        return queryById(user.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User insert(User user) {
        this.userMapper.insert(user);
        return user;
    }

    @Override
    public Integer countAll(User user) {
        return this.userMapper.countAll(user);
    }
}
