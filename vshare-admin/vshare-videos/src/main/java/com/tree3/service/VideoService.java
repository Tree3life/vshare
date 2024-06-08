package com.tree3.service;

import com.tree3.pojo.entity.Video;
import com.tree3.pojo.entity.VideoDTO;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-10 20:31:19
 */
public interface VideoService {

    List<Video> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param video 实例对象
     * @return
     */
    List<VideoDTO> queryAll(VideoDTO video);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-10 20:31:19
     */
    Video queryVideo(Video video);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    Video queryById(Integer id);


    /**
     * 修改数据
     *
     * @param video 实例对象
     */
    Video update(Video video);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param video 实例对象
     */
    Video insert(Video video);

    Integer countAll(VideoDTO video);
}
