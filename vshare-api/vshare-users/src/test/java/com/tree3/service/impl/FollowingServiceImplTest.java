package com.tree3.service.impl;

import com.tree3.ApiUsersApplication;
import com.tree3.pojo.entity.Following;
import com.tree3.service.FollowingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/19 21:09 </p>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiUsersApplication.class)
public class FollowingServiceImplTest {

    @Autowired
    private FollowingService followingService;

    @Test
    public void queryAll() {
        System.out.println(followingService.queryAll());
    }

    @Test
    public void testQueryAll() {
        System.out.println(followingService.queryAll(new Following(), 1, Integer.MAX_VALUE));
    }

    @Test
    public void queryFollowing() {
        Following following = new Following();
        following.setFollowingId(1);
        System.out.println(followingService.queryFollowing(following));
    }

    @Test
    public void queryById() {
        System.out.println(followingService.queryById(1));
    }


}