package com.tree3.service.impl;

import com.tree3.client.VideosClient;
import com.tree3.constants.RedisPrefix;
import com.tree3.dao.FavoriteMapper;
import com.tree3.dao.PlayedMapper;
import com.tree3.pojo.entity.Favorite;
import com.tree3.pojo.entity.Played;
import com.tree3.pojo.entity.User;
import com.tree3.dao.UserMapper;
import com.tree3.pojo.vo.VideoVO;
import com.tree3.service.UserService;
import com.tree3.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author rupert
 * @since 2024-03-13 12:10:44
 */
@Slf4j
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private VideosClient videosClient;
    private StringRedisTemplate stringRedisTemplate;
    private PlayedMapper playedMapper;
    private FavoriteMapper favoriteMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, VideosClient videosClient, StringRedisTemplate stringRedisTemplate, PlayedMapper playedMapper, FavoriteMapper favoriteMapper) {
        this.userMapper = userMapper;
        this.videosClient = videosClient;
        this.stringRedisTemplate = stringRedisTemplate;
        this.playedMapper = playedMapper;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public List<User> queryAll(User user) {
        return userMapper.queryAll(user);
    }

    @Override
    public User queryUser(User user) {
        return userMapper.queryUser(user);
    }


    @Override
    public User queryById(Integer id) {
        return userMapper.queryById(id);
    }

    @Override
    public User update(User user) {
        this.userMapper.update(user);
        return queryById(user.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User insert(User user) {
        this.userMapper.insert(user);
        return user;
    }

    @Override
    public List<VideoVO> queryVideo(User user) {
        List<VideoVO> videoVOS = videosClient.queryByUserId(user);
        return videoVOS;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void playedService(String videoId, User user) {
        //仅记录登录状态用户的播放次数
        if (!ObjectUtils.isEmpty(user)) {
            Played played = new Played();
            played.setUid(user.getId());
            Date now = new Date();
            played.setCreatedAt(now);
            played.setUpdatedAt(now);
            played.setVideoId(Integer.valueOf(videoId));
            playedMapper.insert(played);
            log.info("当前用户的播放记录保存成功,信息为: {}", JSONUtils.toJsonStr(played));
        }


        int a = 1 / 0;

        //将此命令放置到最后执行，防止中间代码执行出现异常后
        // redis 对数据库进行了写操作，而mysql没有写
        //execute a redis transaction
        List<Object> txResults = stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().increment(RedisPrefix.VIDEO_PLAYED_COUNT_PREFIX + videoId);

                // This will contain the results of all operations in the transaction
                return operations.exec();
            }
        });
    }

    /**
     * 自行解决的方案
     *
     * @param videoId
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    public void playedService2(String videoId, User user) {
        // optimized：优化在redis中使用hash结构 ,    使播放次数+1 (Rupert，2024/6/8 )
        stringRedisTemplate.opsForValue().increment(RedisPrefix.VIDEO_PLAYED_COUNT_PREFIX + videoId);

        //1.try cache 捕获除redis操作外的代码
        //2.捕获异常之后执行与redis操作相反的操作

        try {
            int a = 1 / 0;

            //仅记录登录状态用户的播放次数
            if (!ObjectUtils.isEmpty(user)) {
                Played played = new Played();
                played.setUid(user.getId());
                Date now = new Date();
                played.setCreatedAt(now);
                played.setUpdatedAt(now);
                played.setVideoId(Integer.valueOf(videoId));
                playedMapper.insert(played);
                log.info("当前用户的播放记录保存成功,信息为: {}", JSONUtils.toJsonStr(played));
            }
        } catch (Exception e) {
            //回滚redis的操作，执行与上述redis相反的操作
            stringRedisTemplate.opsForValue().decrement(RedisPrefix.VIDEO_PLAYED_COUNT_PREFIX + videoId);
            e.printStackTrace();

            //将上述操作的异常重新抛出去，以防止@Transactional管理的事务失效
            throw new RuntimeException(e);
        }

    }

    /**
     * 点赞
     *
     * @param token
     * @param videoId
     * @param user
     */
    @Override
    public void liked(String token, Integer videoId, User user) {
        //2.将视频的点赞数 自增 ;操作redis  todo 使用hash结构优化
        stringRedisTemplate.opsForValue().increment(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + videoId);

        //3.将当前用户点赞视频列表放入redis中 用set集合存储用户点赞过的视频; 操作redis
        String value = String.valueOf(videoId);
        stringRedisTemplate.opsForSet().add(RedisPrefix.USER_LIKE_PREFIX + user.getId(), value);

        //4.点赞与不喜欢 是互斥的，点赞的同时要将视频从user的dislike列表中将视频删除
        if (stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKE_PREFIX + user.getId().toString(), value)) {
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(), value);
        }
    }

    @Override
    public void disLiked(String token, String videoId, User user) {
        //1.获取用户信息
        log.info("接收的到视频id: {}", videoId);

        //2.将当前用户喜欢的列表中该视频移除掉
        stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_LIKE_PREFIX + user.getId(), videoId);

        //3.将视频点赞次数-1
        String count = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + videoId);
        if (!StringUtils.isEmpty(count) && Integer.valueOf(count) > 0) {
            stringRedisTemplate.opsForValue().decrement(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + videoId);
        }
    }

    @Override
    public void disLikeVidel(User user, String token, Integer videoId) {
        //2.放入当前用户不喜欢的列表
        String vId = String.valueOf(videoId);
        stringRedisTemplate.opsForSet().add(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(), vId);

        //3.判断之前是否点击过喜欢该视频
        if (stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_LIKE_PREFIX + user.getId(), vId)) {
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_LIKE_PREFIX + user.getId(), vId);//从喜欢中列表中删除

            String videoCountStr = stringRedisTemplate.opsForValue().get(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + vId);
            if (!StringUtils.isEmpty(videoCountStr) && Integer.valueOf(videoCountStr) > 0) {
                stringRedisTemplate.opsForValue().decrement(RedisPrefix.VIDEO_LIKE_COUNT_PREFIX + vId);//当前视频喜欢次数-1
            }
        }
    }

    @Override
    public void cancelDislikeVideo(User user, String token, Integer videoId) {
        //2.将当前视频从用户不喜欢的列表中移除掉
        String vId = String.valueOf(videoId);
        if (stringRedisTemplate.opsForSet().isMember(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(), vId)) {
            stringRedisTemplate.opsForSet().remove(RedisPrefix.USER_DISLIKE_PREFIX + user.getId(), vId);
        }
    }

    @Override
    public List<VideoVO> playedHistory(Integer page, Integer pageSize, User user) {
        List<Integer> videos = new ArrayList<>();
        Played query = new Played();
        query.setUid(user.getId());
        query.setOffset((page - 1) * pageSize);
        query.setPageSize(pageSize);
        List<Played> played = playedMapper.queryAll(query);
        if (!ObjectUtils.isEmpty(played)) {
            videos = played.stream()
                    .map(Played::getVideoId)
                    .collect(Collectors.toList());
        }

        return videosClient.getVideos(videos);
    }

    @Override
    public List<VideoVO> favoriteVideos(int i, User user) {
        List<Integer> collect = new ArrayList<>();
        Favorite query = new Favorite();
        query.setUid(user.getId());

        List<Favorite> favorites = favoriteMapper.queryAll(query);
        if (!ObjectUtils.isEmpty(favorites)) {
            collect = favorites.stream().map(Favorite::getVideoId).collect(Collectors.toList());
        }

        return videosClient.getVideos(collect);
    }
}
