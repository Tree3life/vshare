package com.tree3.dao;

import com.tree3.ApiUsersApplication;
import com.tree3.pojo.entity.Following;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/19 16:30 </p>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiUsersApplication.class)
public class FollowingMapperTest {
    @Resource
    private FollowingMapper followingMapper;

    @Test
    public void queryById() {
        log.info("查询结果queryById：{}", JSONUtils.toJsonStr(followingMapper.queryById(1)));
    }


    @Test
    public void testQueryFollowing() {
        Following following = new Following();
        following.setId(2);

        log.info("查询结果queryById：{}", JSONUtils.toJsonStr(followingMapper.queryFollowing(following)));

    }

    @Test
    public void queryAll() {
        log.info("查询结果queryAll：{}", JSONUtils.toJsonStr(followingMapper.queryFollowings()));
    }

    @Test
    public void queryAllByPage() {
        log.info("查询结果queryAllByPage：{}", JSONUtils.toJsonStr(followingMapper.queryFollowings(new Following(), 2, Integer.MAX_VALUE)));
    }

    @Test
    public void count() {
        log.info("查询结果queryAllByPage：{}", JSONUtils.toJsonStr(followingMapper.count(new Following())));
    }


    @Test
    public void deleteById() {
        System.out.println(followingMapper.deleteById(null));
    }

    @Test
    public void update() {
        Following following = new Following();
        following.setUid(2);
        System.out.println(followingMapper.update(following));
    }

    @Test
    public void deleteByEntity() {
        Following following = new Following();
        following.setUid(4);
        following.setFollowingId(2);
        following.setId(3);
        log.info("查询结果deleteByEntity：{}", JSONUtils.toJsonStr(followingMapper.deleteByFollowing(following)));
    }

    @Test
    public void insert() {
        Following following = new Following();
        following.setUid(4);
        following.setFollowingId(2);
        following.setCreatedAt(new Date());
        following.setUpdatedAt(new Date());
        following.setId(3);
        System.out.println(followingMapper.insert(following));
    }

    @Test
    public void insertBatch() {
        Date date = new Date();
        ArrayList<Following> followings = new ArrayList<>();
        followings.add(new Following(null, 2, 2, date, date, date));
        followings.add(new Following(null, 1, 1, date, date, date));
        followings.add(new Following(null, 3, 3, date, date, date));
        followings.add(new Following(null, 1, 1, date, date, date));
        followings.add(new Following(null, 2, 2, date, date, date));
        System.out.println(followings.size());
        System.out.println(followingMapper.insertBatch(followings));
    }

    @Test
    public void insertOrUpdateBatch() {
        Date date = new Date();
        ArrayList<Following> followings = new ArrayList<>();
        followings.add(new Following(17,4, 2 , date, date, date));
        followings.add(new Following(18,4, 2 , date, date, date));
        followings.add(new Following(19,4, 2 , date, date, date));
        followings.add(new Following(20,4, 2 , date, date, date));
        followings.add(new Following(21,4, 2 , date, date, date));
        System.out.println(followings.size());
        System.out.println(followingMapper.insertOrUpdateBatch(followings));
    }

}