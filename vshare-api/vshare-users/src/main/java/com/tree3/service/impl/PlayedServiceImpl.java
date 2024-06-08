package com.tree3.service.impl;

import com.tree3.pojo.entity.Played;
import com.tree3.dao.PlayedMapper;
import com.tree3.service.PlayedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rupert
 * @since 2024-03-16 21:43:35
 */
@Slf4j
@Transactional
@Service("playedService")
public class PlayedServiceImpl implements PlayedService {

    private final PlayedMapper playedMapper;

    @Autowired
    public PlayedServiceImpl(PlayedMapper playedMapper) {
        this.playedMapper = playedMapper;
    }

    @Override
    public List<Played> queryAll() {
        return playedMapper.queryAll();
    }

    @Override
    public List<Played> queryAll(Played played) {
        return playedMapper.queryAll(played);
    }

    @Override
    public Played queryPlayed(Played played) {
        return playedMapper.queryPlayed(played);
    }


    @Override
    public Played queryById(Integer id) {
        return playedMapper.queryById(id);
    }

    @Override
    public Played update(Played played) {
        this.playedMapper.update(played);
        return queryById(played.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return playedMapper.deleteById(id) > 0;
    }

    @Override
    public Played insert(Played played) {
        this.playedMapper.insert(played);
        return played;
    }
}
