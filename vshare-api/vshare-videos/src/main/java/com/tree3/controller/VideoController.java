package com.tree3.controller;

import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.entity.Comment;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.entity.Video;
import com.tree3.pojo.vo.VideoDetailVO;
import com.tree3.pojo.vo.VideoVO;
import com.tree3.service.CommentService;
import com.tree3.service.VideoService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Video表控制器
 *
 * @author rupert
 * @since 2024-03-15 14:31:20
 */
@Slf4j
@RestController
public class VideoController {


    private final VideoService videoService;
    private final CommentService commentService;
    private RedisTemplate redisTemplate;

    @Autowired
    public VideoController(VideoService videoService, CommentService commentService, RedisTemplate redisTemplate) {
        this.videoService = videoService;
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/videos/{video_id}")
    public VideoDetailVO queryDetails(@PathVariable("video_id") Integer id, String token) {
        log.debug("queryDetails------------->id:{}", id);
        log.debug("queryDetails------------->token:{}", token);
        if (ObjectUtils.isEmpty(id)) {
            throw new RuntimeException("video_id缺失");
        }

        return videoService.queryVideoDetailById(id,token);
    }

    @GetMapping("/videos/queryByVids")
    public List<VideoVO> queryByVids(@RequestParam("ids") List<Integer> ids) {
        List<VideoVO> videoVOS = new ArrayList<>();
        log.info("ids: {}", ids);
        if (!ObjectUtils.isEmpty(ids)) {
            videoVOS = videoService.queryVideoVoByVids(ids);
        }
        return videoVOS;
    }

    @GetMapping("/videos")
    public List<VideoVO> videos(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                @RequestParam("category") Integer categoryId) {
        log.info("当前页:{},每页显示记录数:{},分类id:{}", page, rows, categoryId);
        List<VideoVO> videoVOS = videoService.findAllByCategoryId(page, rows, categoryId);
        log.info("复合条件的记录总数: {}", videoVOS.size());
        return videoVOS;
    }

    @PostMapping("videos/publish")
    public Video save(@RequestBody Video video) {
        log.debug("要保存的视频：{}", JSONUtils.toJsonStr(video));
        return videoService.insert(video);
    }


    @PostMapping("videos/queryByUserId") //video
    public List<VideoVO> queryByUserId(@RequestBody User user) {
        if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getId())) {
            throw new RuntimeException("非法参数异常");
        }
        Video video = new Video();
        video.setUid(user.getId());
        List<VideoVO> videoVOS = videoService.queryVideoByUid(video);
        log.info("视频推荐列表数量: {}", videoVOS.size());
        return videoVOS;
    }

    @GetMapping("recommends") //video
    public List<VideoVO> recommends(@RequestParam("page") Integer page, @RequestParam("per_page") Integer per_page) {
        List<VideoVO> videoVOS = videoService.queryAllByLimit(new Video(), page, per_page);
        log.info("视频推荐列表数量: {}", videoVOS.size());
        return videoVOS;
    }


    /**
     *  评论列表
     *
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("videos/{videoId}/comments") //video
    public Map<String, Object> comments(@PathVariable("videoId") Integer videoId,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "per_page", defaultValue = "5") Integer pageSize) {
        log.info("评论接口 videoId: {}", videoId);
        log.info("视频id: {}", videoId);
        log.info("当前页: {}", page);
        log.info("每页显示记录数: {}", pageSize);
        if (videoId == null) {
            throw new IllegalArgumentException("videoId缺失");
        }
        return commentService.queryCommentByVideo(videoId, page, pageSize);
    }


    /**
     * 用户发表评论信息
     */
    @PostMapping("/videos/{video_id}/comments")
    public void comments(
            @PathVariable("video_id") Integer videoId,
            @RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("token异常");
        }

        String tokenKey = RedisPrefix.TOKEN_KEY + token;

        User user = (User) redisTemplate.opsForValue().get(tokenKey);
        log.debug("xxxxxxxxxuser:{}", user);
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("token失效");
        }

        //接受到评论
        log.info("视频id: {}", videoId);
        log.info("评论信息: {}", JSONUtils.toJsonStr(comment));
        Integer userId = user.getId();
        log.info("评论用户信息: {}", userId);
        //设置评论用户信息
        comment.setUid(userId);
        //设置评论视频
        comment.setVideoId(videoId);
        Date now = new Date();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);
        commentService.insert(comment);
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public Video queryById(@PathVariable("id") Integer id) {
        if (log.isTraceEnabled()) {
            log.trace("VideoController-->queryById(Integer  " + id + ")");
        }

        return videoService.queryById(id);
    }

    /**
     * 多条件选择查询
     *
     * @param video 查询条件：可自主设置为Vo对象
     * @return 响应结果
     */
    @GetMapping("/list")
    public List<Video> list(Video video) {
        if (log.isTraceEnabled()) {
            log.trace("VideoController-->list(" + video.toString() + ")");
        }

        return videoService.queryAll(video);
    }

    @PatchMapping("/update")
    public Video update(@RequestBody Video video) {
        if (log.isTraceEnabled()) {
            log.trace("VideoController-->updateVideo (" + video.toString() + ")");
        }

        return videoService.update(video);
    }

    @DeleteMapping("/delete")
    public Boolean deleteById(@RequestBody Video video) {
        if (log.isTraceEnabled()) {
            log.trace("VideoController-->deleteById ( Integer  " + video.getId() + ") ");
        }

        return videoService.deleteById(video.getId());
    }


}
