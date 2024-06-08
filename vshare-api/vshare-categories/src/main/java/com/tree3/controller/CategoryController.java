package com.tree3.controller;

import com.tree3.pojo.entity.Category;
import com.tree3.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Category表控制器
 *
 * @author rupert
 * @since 2024-03-15 10:37:56
 */
@Slf4j
@RequestMapping("categories")
@RestController
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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

    @GetMapping("/{id}")
    public Category queryCategoryById(@PathVariable("id") Integer id) {
        log.debug("queryCategoryById接收到的参数：{}", id);
        return categoryService.queryById(id);
    }

    /**
     * 多条件选择查询
     *
     * @return 响应结果
     */
    @GetMapping
    public List<Category> list() {

        return categoryService.queryCategorys();
    }

    @PatchMapping("/update")
    public Category update(@RequestBody Category category) {
        if (log.isTraceEnabled()) {
            log.trace("CategoryController-->updateCategory (" + category.toString() + ")");
        }

        return categoryService.update(category);
    }

    @DeleteMapping("/delete")
    public Boolean deleteById(@RequestBody Category category) {
        if (log.isTraceEnabled()) {
            log.trace("CategoryController-->deleteById ( Integer  " + category.getId() + ") ");
        }

        return categoryService.deleteById(category.getId());
    }

    @PostMapping("/save")
    public Category save(@RequestBody Category category) {
        if (log.isTraceEnabled()) {
            log.trace("CategoryController-->saveCategory (" + category.toString() + ")");
        }

        return categoryService.insert(category);
    }

}
