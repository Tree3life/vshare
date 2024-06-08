package com.tree3.service;

import com.tree3.pojo.entity.Video;
import com.tree3.pojo.vo.VideoDetailVO;
import com.tree3.pojo.vo.VideoVO;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-15 14:31:20
 */
public interface VideoService {

    List<Video> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param video 实例对象
     */
    List<Video> queryAll(Video video);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-15 14:31:20
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

    List<VideoVO> queryAllByLimit(Video video ,Integer page, Integer pageSize);

    List<VideoVO> queryVideoByUid(Video video);

    List<VideoVO> findAllByCategoryId(Integer page, Integer rows, Integer categoryId);

    VideoDetailVO queryVideoDetailById(Integer id, String token);


    List<VideoVO> queryVideoVoByVids(List<Integer> ids);
}
