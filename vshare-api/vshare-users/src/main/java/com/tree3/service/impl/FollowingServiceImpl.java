package com.tree3.service.impl;

import com.tree3.pojo.entity.Following;
import com.tree3.dao.FollowingMapper;
import com.tree3.service.FollowingService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rupert
 * @since 2024-03-19 21:01:27
 */
@Slf4j
@Transactional
@Service("followingService")
public class FollowingServiceImpl implements FollowingService {

    private final FollowingMapper followingMapper;

    @Autowired
    public FollowingServiceImpl(FollowingMapper followingMapper) {
        this.followingMapper = followingMapper;
    }

    @Override
    public List<Following> queryAll() {
        return followingMapper.queryFollowings();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Following> queryAll(Following following, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return followingMapper.queryFollowings(following, offset, pageSize);
    }


    @Override
    public Following queryFollowing(Following following) {
        return followingMapper.queryFollowing(following);
    }


    @Override
    public Following queryById(Integer id) {
        return followingMapper.queryById(id);
    }

    @Override
    public Following update(Following following) {
        this.followingMapper.update(following);
        return queryById(following.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return followingMapper.deleteById(id) > 0;
    }

    @Override
    public Following insert(Following following) {
        this.followingMapper.insert(following);
        return following;
    }
}
