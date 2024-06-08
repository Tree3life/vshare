package com.tree3.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.tree3.client.CategoryClients;
import com.tree3.client.UserClients;
import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.entity.Category;
import com.tree3.pojo.entity.Favorite;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.entity.Video;
import com.tree3.dao.VideoMapper;
import com.tree3.pojo.vo.VideoDetailVO;
import com.tree3.pojo.vo.VideoVO;
import com.tree3.service.VideoService;
import com.tree3.utils.JSONUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * @author rupert
 * @since 2024-03-15 14:31:20
 */
@Slf4j
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;
    private RabbitTemplate rabbitTemplate;
    private CategoryClients categoryClients;
    private UserClients userClients;
    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper, RabbitTemplate rabbitTemplate, CategoryClients categoryClients, UserClients userClients, StringRedisTemplate stringRedisTemplate, RedisTemplate redisTemplate) {
        this.videoMapper = videoMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.categoryClients = categoryClients;
        this.userClients = userClients;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Video> queryAll() {
        return videoMapper.queryAll();
    }

    @Override
    public List<Video> queryAll(Video video) {
        return videoMapper.queryAll(video);
    }

    @Override
    public Video queryVideo(Video video) {
        return videoMapper.queryVideo(video);
    }


    @Override
    public Video queryById(Integer id) {
        return videoMapper.queryById(id);
    }

    @Override
    public Video update(Video video) {
        this.videoMapper.update(video);
        return queryById(video.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return videoMapper.deleteById(id) > 0;
    }

    @Override
    public Video insert(Video video) {
        Date now = new Date();
        video.setCreatedAt(now);//设置创建日期
        video.setUpdatedAt(now);//设置更新日期
        //Step 1: 保存到数据库
        this.videoMapper.insert(video);

        //利用MQ异步处理,提升系统响应:
        // 将视频信息写入到ES索引库
        //  1.在es中建立6.x  索引index  类型type   映射概念mapping
        //  2.将视频信息写入es
        //Step 2: 发送消息到MQ，通知es对该视频进行 索引分析
        rabbitTemplate.convertAndSend("videos", "", JSONUtils.toJsonStr(toVideoVO(video)));
        return video;
    }

    private VideoVO toVideoVO(Video video) {
        if (null == video) {
            throw new RuntimeException("非法参数");
        }

        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);

        //设置点赞数量
        videoVO.setLikes(0);
//        //视频服务----->调用用户服务 根据用户id查询用户
        User user = userClients.user(video.getUid());//调用用户服务
        log.info("根据id查询用户信息为: {}", JSONUtils.toJsonStr(video));
        videoVO.setUploader(user.getName());//设置用户名
//
//        //视频服务---->调用类别服务  根据类别id查询类别
        Category category = categoryClients.category(video.getCategoryId());
        log.info("根据类别id查询到的类别信息为: {}", JSONUtils.toJsonStr(category));
        videoVO.setCategory(category.getName());
        log.debug("toVideoVO-----------video:{}", JSONUtils.toJsonStr(video));
        log.debug("toVideoVO-----------videoVO:{}", JSONUtils.toJsonStr(videoVO));
        return videoVO;
    }

    @Override
    public List<VideoVO> queryAllByLimit(Video video, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        List<VideoVO> list = this.videoMapper.queryAllByPage(video, offset, pageSize);
        list.forEach((item) -> {
            Integer likes = queryVideoLikes(item);
            item.setLikes(likes);
        });
        return list;
    }

    @Override
    public List<VideoVO> queryVideoByUid(Video video) {
        List<VideoVO> list = this.videoMapper.queryAllByPage(video, 0, Integer.MAX_VALUE);
        list.forEach((item) -> {
            Integer likes = queryVideoLikes(item);
            item.setLikes(likes);
        });
        return list;
    }

    @Override
    public List<VideoVO> queryVideoVoByVids(List<Integer> ids) {
        List<VideoVO> list = this.videoMapper.queryVideoVoByVids(ids);
        list.forEach((item) -> {
            Integer likes = queryVideoLikes(item);
            item.setLikes(likes);
        });
        return list;
    }

    @Override
    public List<VideoVO> findAllByCategoryId(Integer page, Integer rows, Integer categoryId) {
        Video video = new Video();
        video.setCategoryId(categoryId);
        return videoMapper.queryAllByPage(video, (page - 1) * rows, rows);
    }

    @Override
    public VideoDetailVO queryVideoDetailById(Integer id, String token) {
        Video video = videoMapper.queryById(id);
        if (ObjectUtils.isEmpty(video)) {
            return null;
        }
        VideoDetailVO detailVO = new VideoDetailVO();
        BeanUtils.copyProperties(video, detailVO);

        //Step 2: 查询 视频的类别名称
        Category category = categoryClients.category(video.getCategoryId());
        detailVO.setCategory(category.getName());
        log.info("根据类别id查询到的类别信息为: {}", JSONUtils.toJsonStr(category));

        //Step 3: 查询up主详情
        User user = userClients.user(video.getUid());
        detailVO.setUploader(user);
        log.info("根据id查询用户信息为: {}", JSONUtils.toJsonStr(user));

        //Step 5: 设置播放次数
        detailVO.setPlaysCount(0);//初始化默认值为0
        String playedCounts = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_PLAYED_COUNT_PREFIX + id);
        if (!StringUtils.isEmpty(playedCounts)) {
            log.info("当前视频视频播放次数为: {}", playedCounts);
            detailVO.setPlaysCount(Integer.valueOf(playedCounts));
        }

        //Step 6: 设置点赞数
        detailVO.setLikesCount(0);//初始化默认值为0
        String likedCounts = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + video.getId());
        if (!StringUtils.isEmpty(likedCounts)) {
            log.info("当前视频点赞数量为: {}", likedCounts);
            detailVO.setLikesCount(Integer.valueOf(likedCounts));
        }

        //Step 7: 设置是否喜欢
        detailVO.setLiked(true);
        //Step 8: 设置是否不喜欢
        detailVO.setDisliked(false);
        //Step 9: 设置是否收藏

        detailVO.setFavorite(false);
        User o = (User) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY + token);
        if (!ObjectUtils.isEmpty(o)) {
            //设置是否喜欢
            Boolean liked = stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_LIKE_PREFIX + o.getId(), id.toString());
            detailVO.setLiked(liked);
            //设置是否不喜欢
            Boolean disliked = stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKE_PREFIX + o.getId(), id.toString());
            detailVO.setDisliked(disliked);
            //设置是否收藏
            Favorite favorite = userClients.favorite(id, o.getId());
            log.info("是否收藏过该视频: {}", !ObjectUtils.isEmpty(favorite));
            if (!ObjectUtils.isEmpty(favorite)) {
                detailVO.setFavorite(true);
            }
        }

        //endregion
        return detailVO;
    }


    private Integer queryVideoLikes(VideoVO videoVO) {
        String counts = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + videoVO.getId());
        if (!StringUtils.isEmpty(counts)) {
            log.info("当前视频点赞数量为: {}", counts);
            videoVO.setLikes(Integer.valueOf(counts));
        }
        return Integer.valueOf(RandomStringUtils.randomNumeric(3, 4));
    }
}
