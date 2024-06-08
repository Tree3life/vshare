package com.tree3.controller;

import com.tree3.annotation.Token;
import com.tree3.client.VideosClient;
import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.entity.Comment;
import com.tree3.pojo.entity.Favorite;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.entity.Video;
import com.tree3.pojo.vo.VideoVO;
import com.tree3.service.FavoriteService;
import com.tree3.service.PlayedService;
import com.tree3.service.UserService;
import com.tree3.utils.JSONUtils;
import com.tree3.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * User表控制器
 *
 * @author rupert
 * @since 2024-03-13 12:10:44
 */
@Slf4j
@RequestMapping("user")
@RestController
public class UserController {

    private final UserService userService;
    private StringRedisTemplate strRedisTemplate;
    private RedisTemplate redisTemplate;
    private VideosClient videosClient;
    private PlayedService playedService;
    private FavoriteService favoriteService;

    @Autowired
    public UserController(UserService userService, StringRedisTemplate strRedisTemplate, RedisTemplate redisTemplate, VideosClient videosClient, PlayedService playedService, FavoriteService favoriteService) {
        this.userService = userService;
        this.strRedisTemplate = strRedisTemplate;
        this.redisTemplate = redisTemplate;
        this.videosClient = videosClient;
        this.playedService = playedService;
        this.favoriteService = favoriteService;
    }

    /**
     * 用户取消收藏
     */
    @Token
    @DeleteMapping("/favorites/{id}")
    public void cancelCollectVideos(@PathVariable("id") Integer videoId, HttpServletRequest request) {
        log.info("取消收藏视频的id:{}", videoId);
        User user = (User) request.getAttribute("user");
        if (videoId == null || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("视频Id缺失/请先登录！");
        }

        favoriteService.cancelCollectVideos(videoId, user);
    }

    /**
     * 用户收藏视频
     */
    @Token
    @PutMapping("/favorites/{id}")
    public void collectVideos(@PathVariable("id") Integer videoId, HttpServletRequest request) {
        log.info("收藏视频的id:{}", videoId);
        User user = (User) request.getAttribute("user");
        if (videoId == null || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("视频Id缺失/请先登录！");
        }

        favoriteService.collectVideos(videoId, user);
    }

    /**
     * 不喜欢
     *
     * @param videoId
     * @param request
     */
    @Token
    @PutMapping("/disliked/{id}")
    public void dislikeVideo(@PathVariable("id") Integer videoId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.debug("不喜欢----------->videoId：{}，user:{}", videoId, JSONUtils.toJsonStr(user));
        if (StringUtils.isEmpty(videoId) || StringUtils.isEmpty(token) || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("videoId缺失/请先登录");
        }

        userService.disLikeVidel(user, token, videoId);
    }

    /**
     * 取消 不喜欢
     *
     * @param videoId
     * @param request
     */
    @Token
    @DeleteMapping("/disliked/{id}")
    public void cancelDislikeVideo(@PathVariable("id") Integer videoId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.debug("取消不喜欢----------->videoId：{}，user:{}", videoId, JSONUtils.toJsonStr(user));
        if (StringUtils.isEmpty(videoId) || StringUtils.isEmpty(token) || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("videoId缺失/请先登录");
        }

        userService.cancelDislikeVideo(user, token, videoId);
    }

    /**
     * 点赞
     *
     * @param videoId
     * @param request
     */
    @Token
    @PutMapping("/liked/{id}")
    public void liked(@PathVariable("id") Integer videoId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.debug("videoId：{}，user:{}", videoId, JSONUtils.toJsonStr(user));
        if (StringUtils.isEmpty(videoId) || StringUtils.isEmpty(token) || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("videoId缺失/请先登录");
        }

        userService.liked(token, videoId, user);
    }


    /**
     * 取消点赞
     *
     * @param videoId
     * @param request
     */
    @Token
    @DeleteMapping("/liked/{id}")
    public void cancelLiked(@PathVariable("id") String videoId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.debug("videoId：{}，user:{}", videoId, JSONUtils.toJsonStr(user));
        if (StringUtils.isEmpty(videoId) || StringUtils.isEmpty(token) || ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("videoId缺失/请先登录");
        }

        userService.disLiked(token, videoId, user);
    }

    @Token
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/videos")
    public Video upload(MultipartFile file, VideoVO videoVO, HttpServletRequest request) {
        Integer category_id = Integer.valueOf(request.getParameter("category_id"));
        log.debug("++++++++++++++++++++++++++++category_id:{}", category_id);
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.info("收到的VideoVO：{}", JSONUtils.toJsonStr(videoVO));
        log.info("requestz中的参数user：{}", JSONUtils.toJsonStr(user));
        log.info("requestz中的参数token：{}", token);

        if (ObjectUtils.isEmpty(file) || ObjectUtils.isEmpty(videoVO)) {
            throw new RuntimeException("文件/文件信息接收失败！");
        }

        //设置类别id
        videoVO.setCategoryId(category_id);

        //1.获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();
        log.info("文件名称为: {},文件大小为: {}", originalFilename, size);

        String extension = FilenameUtils.getExtension(originalFilename);
        String uuidFileName = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuidFileName + "_" + size + "." + extension;

        //Step 2:  文件重命名，todo 并将文件上传至阿里云oss,返回文件在oss地址
        String url = "";
        try {
            url = OSSUtils.upload(file.getInputStream(), "videos", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String url = "https://阿里云/favor/waner.mp4";
        log.info("上传成功返回的地址: {}", url);
        //阿里云oss截取视频中某一帧作为封面
        String cover = url + "?x-oss-process=video/snapshot,t_300,f_jpg,w_0,h_0,m_fast,ar_auto";
        log.info("阿里云oss根据url截取视频封面: {}", cover);
        //6.设置视频信息
        videoVO.setCover(cover);//设置视频封面
        videoVO.setLink(url);//设置视频地址
        videoVO.setUid(user.getId());//设置发布用户id
        Date now = new Date();
        videoVO.setCreatedAt(now);
        videoVO.setUpdatedAt(now);

        Video target = new Video();
        BeanUtils.copyProperties(videoVO, target);
        log.debug("--------->soruce:{}", JSONUtils.toJsonStr(videoVO));
        log.debug("--------->target:{}", JSONUtils.toJsonStr(target));
//        //调用视频服务
        Video videoResult = videosClient.save(target);
        log.info("视频发布成功之后返回的视频信息: {}", JSONUtils.toJsonStr(videoResult));
        return videoResult;
    }


    @Token
    @GetMapping
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        log.debug("返回的user：{}", JSONUtils.toJsonStr(user));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Token
    @PatchMapping
    public User update(@RequestBody User user, HttpServletRequest request) {
        log.debug("接收到的参数:{}", user);
        User userOld = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");

        String phoneNew = user.getPhone();

        //校验新的手机号
        if (!StringUtils.isEmpty(phoneNew)) {
            String code = strRedisTemplate.opsForValue().get(RedisPrefix.PHONE_KEY + phoneNew);
            if (StringUtils.isEmpty(code)) {
                throw new RuntimeException("验证码已过期");
            }

            //校验与新手机所对应的验证码
            if (!code.equals(user.getCaptcha())) {
                throw new RuntimeException("验证码输入错误");
            }

            //新手机校验通过
            userOld.setPhone(user.getPhone());
            userOld.setPhoneLinked(true);
        }

        if (!StringUtils.isEmpty(user.getName())) {
            userOld.setName(user.getName());
        }
        if (!StringUtils.isEmpty(user.getIntro())) {
            userOld.setIntro(user.getIntro());
        }
        //5.更新用户信息
        userService.update(userOld);
        redisTemplate.opsForValue().set(RedisPrefix.TOKEN_KEY + token, userOld, 10, TimeUnit.DAYS);
        return userOld;
    }


    @GetMapping("demos")
    public String demo() {
        log.debug("vshare-ApiUsers");
        return "vshare-ApiUsers";
    }

    /**
     * 根据用户id视频id查询是否收藏
     *
     * @param videoId
     * @param userId
     * @return
     */
    @GetMapping("/userInfo/favorite")
    public Favorite favorite(@RequestParam("videoId") Integer videoId, @RequestParam("userId") Integer userId) {
        log.info("接收到的视频id {}, 用户id: {}", videoId, userId);
        Favorite query = new Favorite();
        query.setUid(userId);
        query.setVideoId(videoId);
        Favorite favorite = favoriteService.queryFavorite(query);
        log.info("当前返回的收藏对象是否为空: {}", ObjectUtils.isEmpty(favorite));
        return favorite;
    }


    @Token
    @PutMapping("/played/{id}")
    public void played(@PathVariable("id") String videoId, HttpServletRequest request) {


        log.debug("videoId:{}", videoId);
        //1.获取登录用户
        User user = (User) request.getAttribute("user");
        log.debug("获取当前点击用户的信息use:{}", user);

        userService.playedService(videoId, user);
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public User queryById(@PathVariable("id") Integer id) {
        return userService.queryById(id);
    }

    @Token
    @GetMapping("/videos")
    public List<VideoVO> queryVideosByUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        String token = (String) request.getAttribute("token");
        log.debug("获取到的参数user：{}", JSONUtils.toJsonStr(user));
        log.debug("获取到的参数token：{}", token);
        if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(token)) {
            throw new RuntimeException("token失效/用户消息获取失败");
        }

        List<VideoVO> videoVOS = userService.queryVideo(user);

        return videoVOS;
    }

    @Token
    @GetMapping("/favorites")
    public List<VideoVO> favorites(HttpServletRequest request,
                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "per_page", defaultValue = "5") Integer pageSize) {
        User user = (User) request.getAttribute("user");
        log.info("收藏列表：当前页: {} 每页显示记录: {},user:{}", page, pageSize, JSONUtils.toJsonStr(user));
        List<VideoVO> favorites = userService.favoriteVideos((page - 1) * pageSize, user);
        return favorites;
    }

    @Token
    @GetMapping("/played")
    public List<VideoVO> played(HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "per_page", defaultValue = "5") Integer pageSize) {
        User user = (User) request.getAttribute("user");
        log.info("播放历史：当前页: {} 每页显示记录: {},user:{}", page, pageSize, JSONUtils.toJsonStr(user));

        if (ObjectUtils.isEmpty(user)) {
            throw new IllegalArgumentException("请先登录！！！");
        }
        List<VideoVO> history = userService.playedHistory(page, pageSize, user);
        return history;
    }

//
//    /**
//     * 用户发表评论信息
//     */
//    @PostMapping("/comment/{userId}/{videoId}")
//    public void comments(@PathVariable("userId") Integer userId, @PathVariable("videoId") Integer videoId, @RequestBody Comment comment) {
//        if (null == userId || videoId == null || ObjectUtils.isEmpty(comment)) {
//            throw new IllegalArgumentException("无效的用户评论添加");
//        }
//
//        //接受到评论
//        log.info("视频id: {}", videoId);
//        log.info("评论信息: {}", JSONUtils.toJsonStr(comment));
//        log.info("评论用户信息: {}", userId);
//        System.out.println("user服务---------用户发表评论信息");
//        System.out.println("user服务---------用户发表评论信息");
//        System.out.println("user服务---------用户发表评论信息");
//        videosClient.addComment(userId, videoId, comment);
//    }
//
//
//    /**
//     * 视频评论列表
//     */
//    @GetMapping("/comments")
//    public Map<String, Object> comments(@RequestParam("videoId") Integer videoId,
//                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
//                                        @RequestParam(value = "per_page", defaultValue = "15") Integer pageSize) {
//        log.info("视频id: {}", videoId);
//        log.info("当前页: {}", page);
//        log.info("每页显示记录数: {}", pageSize);
//
//        System.out.println("user服务---------视频评论列表");
//        System.out.println("user服务---------视频评论列表");
//        System.out.println("user服务---------视频评论列表");
//        return videosClient.comments(videoId, page, pageSize);
//    }


//    @DeleteMapping("/delete")
//    public Boolean deleteById(@RequestBody User user) {
//        if (log.isTraceEnabled()) {
//            log.trace("UserController-->deleteById ( Integer  " + user.getId() + ") ");
//        }
//
//        return userService.deleteById(user.getId());
//    }
//
//    @PostMapping("/save")
//    public User save(@RequestBody User user) {
//        log.debug("用户登录信息：{}", JSONUtils.toJsonStr(user));
//
//        User userDB = userService.queryUser(user);
//
//
//        return userService.insert(user);
//    }

//    /**
//     * 多条件选择查询
//     *
//     * @param user 查询条件：可自主设置为Vo对象
//     * @return 响应结果
//     */
//    @GetMapping("/list")
//    public List<User> list(User user) {
//        if (log.isTraceEnabled()) {
//            log.trace("UserController-->list(" + user.toString() + ")");
//        }
//
//        return userService.queryAll(user);
//    }
}
