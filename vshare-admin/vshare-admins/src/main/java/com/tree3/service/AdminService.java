package com.tree3.service;

import com.tree3.pojo.entity.Admin;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-08 09:27:41
 */
public interface AdminService {

    List<Admin> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param admin 实例对象
     */
    List<Admin> queryAll(Admin admin);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-08 09:27:41
     */
    Admin queryAdmin(Admin admin);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Admin queryById(Integer id);


    /**
     * 修改数据
     *
     * @param admin 实例对象
     */
    Admin update(Admin admin);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param admin 实例对象
     */
    Admin insert(Admin admin);


    /**
     * 登录方法
     * @param admin
     * @return
     */
    Admin login(Admin admin);
}
