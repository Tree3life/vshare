package com.tree3.service;

import com.tree3.pojo.entity.Category;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-09 18:50:48
 */
public interface CategoryService {

    List<Category> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param category 实例对象
     */
    List<Category> queryAll(Category category);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-09 18:50:48
     */
    Category queryCategory(Category category);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Category queryById(Integer id);


    /**
     * 修改数据
     *
     * @param category 实例对象
     */
    Category update(Category category);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param category 实例对象
     */
    Category insert(Category category);

    List<Category> queryByFirstLevel();
}
