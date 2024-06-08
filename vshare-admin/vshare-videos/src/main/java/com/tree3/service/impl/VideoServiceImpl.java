package com.tree3.service.impl;

import com.tree3.dao.VideoMapper;
import com.tree3.pojo.entity.Video;
import com.tree3.pojo.entity.VideoDTO;
import com.tree3.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rupert
 * @since 2024-03-10 20:31:19
 */
@Slf4j
@Transactional
@Service("videoService")
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;

    @Autowired
    public VideoServiceImpl(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    @Override
    public List<Video> queryAll() {
        return videoMapper.queryAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VideoDTO> queryAll(VideoDTO video) {
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
        this.videoMapper.insert(video);
        return video;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer countAll(VideoDTO video) {
        return this.videoMapper.countAll(video);
    }

}
