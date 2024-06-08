package com.tree3.service.impl;

import com.tree3.pojo.entity.Favorite;
import com.tree3.dao.FavoriteMapper;
import com.tree3.pojo.entity.User;
import com.tree3.service.FavoriteService;
import com.tree3.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author rupert
 * @since 2024-03-17 18:02:43
 */
@Slf4j
@Transactional
@Service("favoriteService")
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public List<Favorite> queryAll() {
        return favoriteMapper.queryAll();
    }

    @Override
    public List<Favorite> queryAll(Favorite favorite) {
        return favoriteMapper.queryAll(favorite);
    }

    @Override
    public Favorite queryFavorite(Favorite favorite) {
        return favoriteMapper.queryFavorite(favorite);
    }


    @Override
    public Favorite queryById(Integer id) {
        return favoriteMapper.queryById(id);
    }

    @Override
    public Favorite update(Favorite favorite) {
        this.favoriteMapper.update(favorite);
        return queryById(favorite.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return favoriteMapper.deleteById(id) > 0;
    }

    @Override
    public Favorite insert(Favorite favorite) {
        this.favoriteMapper.insert(favorite);
        return favorite;
    }

    @Override
    public void collectVideos(Integer videoId, User user) {
        Favorite query = new Favorite();
        query.setUid(user.getId());
        query.setVideoId(videoId);


        Favorite favorite = favoriteMapper.queryFavorite(query);
        if (ObjectUtils.isEmpty(favorite)) {
            Date now = new Date();
            query.setCreatedAt(now);
            query.setUpdatedAt(now);
            log.info("要添加的收藏记录：{}",JSONUtils.toJsonStr(query));
            favoriteMapper.insert(query);
            log.info("收藏视频成功: {}", JSONUtils.toJsonStr(query));
        }
    }

    @Override
    public void cancelCollectVideos(Integer videoId, User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("用户id不存在！！！！");
        }
        favoriteMapper.deleteByUIdAndVid(videoId, user.getId());
    }
}
