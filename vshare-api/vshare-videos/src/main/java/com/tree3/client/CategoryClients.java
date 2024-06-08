package com.tree3.client;

import com.tree3.pojo.entity.Category;
import com.tree3.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/16 18:24 </p>
 */

@FeignClient("API-CATEGORYS")
public interface CategoryClients {

    @GetMapping("categories/get/{id}")
    Category category(@PathVariable("id")Integer categoryId);
}
