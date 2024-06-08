package com.tree3.service.impl;

import com.tree3.dao.CategoryMapper;
import com.tree3.pojo.entity.Category;
import com.tree3.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author rupert
 * @since 2024-03-09 18:50:48
 */
@Slf4j
@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

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
    public List<Category> queryByFirstLevel() {
        return categoryMapper.queryByFirstLevel();
    }
}
