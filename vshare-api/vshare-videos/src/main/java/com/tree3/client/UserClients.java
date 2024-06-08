package com.tree3.client;

import com.tree3.pojo.entity.Favorite;
import com.tree3.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/16 18:24 </p>
 */

@FeignClient("API-USERS")
public interface UserClients {

    @GetMapping("user/get/{id}")
    User user(@PathVariable("id") Integer uid);

    @GetMapping("user/userInfo/favorite")
    Favorite favorite(@RequestParam("videoId") Integer videoId, @RequestParam("userId") Integer userId);

}
