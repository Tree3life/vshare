package com.tree3.service;

import com.tree3.pojo.entity.User;
import com.tree3.pojo.vo.VideoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rupert
 * @since 2024-03-13 12:10:44
 */
public interface UserService {

    List<User> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param user 实例对象
     */
    List<User> queryAll(User user);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-13 12:10:44
     */
    User queryUser(User user);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    User queryById(Integer id);


    /**
     * 修改数据
     *
     * @param user 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param user 实例对象
     */
    User insert(User user);

    List<VideoVO> queryVideo(User user);

    void playedService(String videoId, User user);

    void liked(String token, Integer videoId, User user);

    void disLiked(String token, String videoId, User user);

    void disLikeVidel(User user, String token, Integer videoId);

    void cancelDislikeVideo(User user, String token, Integer videoId);

    List<VideoVO> playedHistory(Integer page, Integer pageSize, User user);

    List<VideoVO> favoriteVideos(int i, User user);
}
