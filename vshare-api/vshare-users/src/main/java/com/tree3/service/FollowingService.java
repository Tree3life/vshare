package com.tree3.service;

import com.tree3.pojo.entity.Following;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-19 21:01:27
 */
public interface FollowingService {

    List<Following> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param following 实例对象
     */
    List<Following> queryAll(Following following, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-19 21:01:27
     */
    Following queryFollowing(Following following);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Following queryById(Integer id);


    /**
     * 修改数据
     *
     * @param following 实例对象
     */
    Following update(Following following);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param following 实例对象
     */
    Following insert(Following following);
}
