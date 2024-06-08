package com.tree3.client;

import com.tree3.pojo.entity.Comment;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.entity.Video;
import com.tree3.pojo.vo.VideoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/15 22:34 </p>
 */
@FeignClient("API-VIDEOS")
public interface VideosClient {

    @PostMapping("videos/publish")
    Video save(@RequestBody Video video);

    @PostMapping("videos/queryByUserId")
    List<VideoVO> queryByUserId(@RequestBody User user);

    @GetMapping("videos/queryByVids")
    List<VideoVO> getVideos(@RequestParam("ids") List<Integer> vids);

    /**
     * 评论列表
     *
     * @param videoId
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("videos/{videoId}/comments")
    //video
    Map<String, Object> comments(@PathVariable("videoId") Integer videoId,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "per_page", defaultValue = "5") Integer pageSize);
    /**
     *  远程调用 用户发表评论信息 路径/user/comment/{userId}/{videoId}
     * http://192.168.10.101:9100/api/videos/{video_id}/comments
     */
    @PostMapping("/videos/comment/{userId}/{videoId}")
    void addComment(
            @PathVariable("userId") Integer userId,
            @PathVariable("videoId") Integer videoId,
            @RequestBody Comment comment);
}
