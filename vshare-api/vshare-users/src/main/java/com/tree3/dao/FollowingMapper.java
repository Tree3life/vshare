package com.tree3.dao;

import com.tree3.pojo.entity.Following;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Following 表数据库访问层
 *
 * @author Rupert
 * @since 2024-03-21 19:01:35
 */
@Mapper
public interface FollowingMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Following queryById(Integer id);

    /**
     * 条件查询单个元素
     * !!当查询结果不唯一时 会抛出异常 org.apache.ibatis.exceptions.TooManyResultsException
     *
     * @throws
     * @author rupert
     * @date 2024-03-21 19:01:35
     * @see org.apache.ibatis.exceptions.TooManyResultsException
     */
    Following queryFollowing(Following following);


    /**
     * 查询所有
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-03-21 19:01:35
     */
    List<Following> queryFollowings();


    /**
     * 实体作为筛选条件分页查询数据
     * 特别的-->查询所有：( null,  null,  null) 等价于 {@link FollowingMapper#queryFollowings()}
     *
     * @param following 实例对象
     * @param offset    数据偏移量（从该条数据开始查询）
     * @param pageSize  数据条目的数量
     * @return 对象列表
     * @author rupert
     * @date 2024-03-21 19:01:35
     */
    List<Following> queryFollowings(Following following, Integer offset, Integer pageSize);

    /**
     * 统计总行数<br/>
     * 与
     * {@link FollowingMapper#queryFollowings(Following following, Integer offset, Integer pageSize)}配合使用，完成分页查询
     *
     * @param following 查询条件
     * @return 总行数
     */
    long count(Following following);


    /**
     * 修改数据
     *
     * @param following 实例对象
     * @return 影响行数
     */
    int update(Following following);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * @param following 筛选条件
     * @return 影响行数
     */
    int deleteByFollowing(Following following);

    /**
     * 新增数据
     *
     * @param following 实例对象
     * @return 影响行数
     */
    int insert(Following following);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Following> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("followings") List<Following> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Following> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("followings") List<Following> entities);

}

