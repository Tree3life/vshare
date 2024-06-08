package com.tree3.controller;

import com.tree3.service.VideoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SearchController {


    @Autowired
    private VideoSearchService videoSearchService;

    @GetMapping("/search/videos")
    public Map<String,Object> videos(@RequestParam(value = "q") String q,
                                     @RequestParam(value = "page",defaultValue = "1") Integer page,
                                     @RequestParam(value = "per_page",defaultValue = "5") Integer rows){
        log.debug("接收到的参数：{}，{}，{}", q, page, rows);
        return videoSearchService.videos(q,page,rows);
    }

}