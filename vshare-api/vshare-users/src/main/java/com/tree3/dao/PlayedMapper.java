package com.tree3.dao;

import com.tree3.pojo.entity.Played;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Played 表数据库访问层
 *
 * @author Rupert
 * @since 2024-03-16 21:43:34
 */
@Mapper
public interface PlayedMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Played queryById(Integer id);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-16 21:43:34
     */
    Played queryPlayed(Played played);


    /**
     * 查询全部数据
     * 分页使用MyBatis的插件实现
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-03-16 21:43:34
     */
    List<Played> queryAll();

    /**
     * 实体作为筛选条件查询数据
     *
     * @param played 实例对象
     * @return 对象列表
     * @author rupert
     * @date 2024-03-16 21:43:34
     */
    List<Played> queryAll(Played played);

    /**
     * 统计总行数
     *
     * @param played 查询条件
     * @return 总行数
     */
    long count(Played played);


    /**
     * 修改数据
     *
     * @param played 实例对象
     * @return 影响行数
     */
    int update(Played played);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 新增数据
     *
     * @param played 实例对象
     * @return 影响行数
     */
    int insert(Played played);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Played> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Played> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Played> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Played> entities);
}

