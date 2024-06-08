package com.tree3.controller;


import com.tree3.config.MethodExporter;
import com.tree3.exception.BussinessException;
import com.tree3.pojo.entity.Following;
import com.tree3.service.FollowingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Following表控制器
 *
 * @author rupert
 * @since 2024-03-19 21:01:27
 */
@Slf4j
@RequestMapping("followings")
@RestController
public class FollowingController {


    private final FollowingService followingService;

    @Autowired
    public FollowingController(FollowingService followingService) {
        this.followingService = followingService;
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @MethodExporter
    @GetMapping("/get/{id}")
    public Following queryById(@PathVariable("id") Integer id) {
        return followingService.queryById(id);
    }


    /**
     * 多条件选择查询
     *
     * @param following 查询条件：可自主设置为Vo对象
     * @return 响应结果
     */
    @MethodExporter
    @GetMapping("/list")
    public List<Following> list(Following following,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        BussinessException bussinessException = new BussinessException();
        return followingService.queryAll(following, page, pageSize);
    }

    @MethodExporter
    @PatchMapping("/update")
    public Following update(@RequestBody Following following) {
        return followingService.update(following);
    }

    @MethodExporter
    @DeleteMapping("/delete")
    public Boolean deleteById(@RequestBody Following following) {
        return followingService.deleteById(following.getId());
    }

    @MethodExporter
    @PostMapping("/save")
    public Following save(@RequestBody Following following) {
        return followingService.insert(following);
    }

}
