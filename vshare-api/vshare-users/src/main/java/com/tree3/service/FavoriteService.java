package com.tree3.service;

import com.tree3.pojo.entity.Favorite;
import com.tree3.pojo.entity.User;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-17 18:02:43
 */
public interface FavoriteService {

    List<Favorite> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param favorite 实例对象
     */
    List<Favorite> queryAll(Favorite favorite);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-17 18:02:43
     */
    Favorite queryFavorite(Favorite favorite);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Favorite queryById(Integer id);


    /**
     * 修改数据
     *
     * @param favorite 实例对象
     */
    Favorite update(Favorite favorite);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param favorite 实例对象
     */
    Favorite insert(Favorite favorite);

    void collectVideos(Integer videoId, User user);

    void cancelCollectVideos(Integer videoId, User user);
}
