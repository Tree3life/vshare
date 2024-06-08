package com.tree3.dao;

import com.tree3.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comment 表数据库访问层
 *
 * @author Rupert
 * @since 2024-03-17 21:03:55
 */
@Mapper
public interface CommentMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Comment queryById(Integer id);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-17 21:03:55
     */
    Comment queryComment(Comment comment);


    /**
     * 查询全部数据
     * 分页使用MyBatis的插件实现
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-03-17 21:03:55
     */
    List<Comment> queryAll();

    /**
     * 实体作为筛选条件查询数据
     *
     * @param comment 实例对象
     * @return 对象列表
     * @author rupert
     * @date 2024-03-17 21:03:55
     */
    List<Comment> queryAll(Comment comment);

    /**
     * 统计总行数
     *
     * @param comment 查询条件
     * @return 总行数
     */
    long count(Comment comment);


    /**
     * 修改数据
     *
     * @param comment 实例对象
     * @return 影响行数
     */
    int update(Comment comment);

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
     * @param comment 实例对象
     * @return 影响行数
     */
    int insert(Comment comment);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Comment> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Comment> entities);

    List<Comment> queryAllComment(Comment comment, int offset, Integer pageSize);

    List<Comment> findByParentId(Integer id);

    Long findByVideoIdCounts(Integer videoId);
}

