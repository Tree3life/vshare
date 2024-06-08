package com.tree3.controller;

import com.tree3.pojo.entity.Video;
import com.tree3.pojo.entity.VideoDTO;
import com.tree3.service.VideoService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Video表控制器
 *
 * @author rupert
 * @since 2024-03-10 20:24:06
 */
@Slf4j
@RequestMapping("videos")
@RestController
public class VideoController {


    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
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
     * @param  ：可自主设置为Vo对象
     * @return 响应结果
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", required = true, defaultValue = "5") Integer perPage,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category_id", required = false) Integer categoryId,
            @RequestParam(name = "uploader_name", required = false) String uploaderName
    ) {
        Map<String, Object> result = new HashMap<>();

        VideoDTO video = new VideoDTO();
        video.setOffset((page - 1) * perPage);
        video.setPageSize(perPage);
        video.setId(id);
        video.setTitle(name);
        video.setCategoryId(categoryId);
        video.setUserName(uploaderName);

        log.debug("收到的参数:{}", JSONUtils.toJsonStr(video));
        List<VideoDTO> videos = videoService.queryAll(video);
        result.put("total_count", videoService.countAll(video));
        result.put("items", videos);

        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @PostMapping("/save")
    public Video save(@RequestBody Video video) {
        if (log.isTraceEnabled()) {
            log.trace("VideoController-->saveVideo (" + video.toString() + ")");
        }

        return videoService.insert(video);
    }

}
