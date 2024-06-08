package com.tree3.controller;

import com.tree3.pojo.entity.Category;
import com.tree3.service.CategoryService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Category表控制器
 *
 * @author rupert
 * @since 2024-03-09 18:50:48
 */
@Slf4j
@RequestMapping("categories")
@RestController
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public Category queryById(@PathVariable("id") Integer id) {
        if (log.isTraceEnabled()) {
            log.trace("CategoryController-->queryById(Integer  " + id + ")");
        }

        return categoryService.queryById(id);
    }

    /**
     * 查询所以类别
     *
     * @return 响应结果
     */
    @GetMapping
    public ResponseEntity<List<Category>> list() {
        return new ResponseEntity<List<Category>>(categoryService.queryByFirstLevel(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> update(@RequestBody Category category, @PathVariable("id") Integer id) {
        log.debug("接收的category信息：{}", JSONUtils.toJsonStr(category));

        category.setId(id);
        return new ResponseEntity<>(categoryService.update(category), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Integer id) {
        log.debug("删除目标的id:{}", id);
        return new  ResponseEntity<>(categoryService.deleteById(id),HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category) {
        log.debug("收到的类别参数:{}", JSONUtils.toJsonStr(category));

        if (ObjectUtils.isEmpty(category) || StringUtils.isEmpty(category.getName())) {
            throw new RuntimeException("请检查`类别名/parent_id`是否为空");
        }
        Date now = new Date();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        category = categoryService.insert(category);
        log.info("添加之后类别信息: {}", JSONUtils.toJsonStr(category));

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

}
