package com.tree3.service.impl;

import com.tree3.pojo.entity.Category;
import com.tree3.dao.CategoryMapper;
import com.tree3.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rupert
 * @since 2024-03-15 10:37:56
 */
@Slf4j
@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> queryAll() {
        return categoryMapper.queryAll();
    }

    @Override
    public List<Category> queryAll(Category category) {
        return categoryMapper.queryAll(category);
    }

    @Override
    public Category queryCategory(Category category) {
        return categoryMapper.queryCategory(category);
    }


    @Override
    public Category queryById(Integer id) {
        return categoryMapper.queryById(id);
    }

    @Override
    public Category update(Category category) {
        this.categoryMapper.update(category);
        return queryById(category.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public Category insert(Category category) {
        this.categoryMapper.insert(category);
        return category;
    }

    @Override
    public List<Category> queryCategorys() {
        return this.categoryMapper.queryCategorys();
    }
}
