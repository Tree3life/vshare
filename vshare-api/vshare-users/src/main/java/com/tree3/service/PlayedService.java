package com.tree3.service;

import com.tree3.pojo.entity.Played;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-16 21:43:34
 */
public interface PlayedService {

    List<Played> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param played 实例对象
     */
    List<Played> queryAll(Played played);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-16 21:43:34
     */
    Played queryPlayed(Played played);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Played queryById(Integer id);


    /**
     * 修改数据
     *
     * @param played 实例对象
     */
    Played update(Played played);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param played 实例对象
     */
    Played insert(Played played);
}
