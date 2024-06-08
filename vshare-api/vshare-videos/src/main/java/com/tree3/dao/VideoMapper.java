package com.tree3.dao;

import com.tree3.pojo.entity.Video;
import com.tree3.pojo.vo.VideoDetailVO;
import com.tree3.pojo.vo.VideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Video 表数据库访问层
 *
 * @author Rupert
 * @since 2024-03-15 14:31:19
 */
@Mapper
public interface VideoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Video queryById(Integer id);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-03-15 14:31:19
     */
    Video queryVideo(Video video);


    /**
     * 查询全部数据
     * 分页使用MyBatis的插件实现
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-03-15 14:31:19
     */
    List<Video> queryAll();

    /**
     * 实体作为筛选条件查询数据
     *
     * @param video 实例对象
     * @return 对象列表
     * @author rupert
     * @date 2024-03-15 14:31:19
     */
    List<Video> queryAll(Video video);

    /**
     * 统计总行数
     *
     * @param video 查询条件
     * @return 总行数
     */
    long count(Video video);


    /**
     * 修改数据
     *
     * @param video 实例对象
     * @return 影响行数
     */
    int update(Video video);

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
     * @param video 实例对象
     * @return 影响行数
     */
    int insert(Video video);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Video> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Video> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Video> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Video> entities);

    /**
     * 分页模糊查询
     *
     * @param video
     * @param offset
     * @param pageSize
     * @return
     */
    List<VideoVO> queryAll(Video video, Integer offset, Integer pageSize);

    List<VideoVO> queryAllByPage(Video video, Integer offset, Integer pageSize);
    List<VideoVO> queryVideoVoByVids(List<Integer> ids);


//    VideoDetailVO queryVideoDetails(Integer id);
}

