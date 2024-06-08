package com.tree3.service;

import com.tree3.pojo.entity.Comment;
import com.tree3.pojo.vo.VideoVO;

import java.util.List;
import java.util.Map;

/**
 * @author Rupert
 * @since 2024-03-17 21:03:55
 */
public interface CommentService {

    List<Comment> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param comment 实例对象
     */
    List<Comment> queryAll(Comment comment);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-17 21:03:55
     */
    Comment queryComment(Comment comment);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Comment queryById(Integer id);


    /**
     * 修改数据
     *
     * @param comment 实例对象
     */
    Comment update(Comment comment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param comment 实例对象
     */
    Comment insert(Comment comment);

    Map<String, Object> queryCommentByVideo(Integer videoId, Integer page, Integer pageSize);
}
