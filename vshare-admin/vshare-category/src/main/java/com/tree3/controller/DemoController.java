package com.tree3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 * <a>@Author: RuADMINSpert</ a>
 * <p>创建时间: 2024/3/6 18:30 </p>
 */
@RestController
@RequestMapping("demos")
public class DemoController {
    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping
    public String demo() {
        log.debug("vshare-category");
        return "vshare-category";
    }
}
