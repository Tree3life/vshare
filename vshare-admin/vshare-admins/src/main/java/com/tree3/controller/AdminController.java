package com.tree3.controller;

import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.AdminDTO;
import com.tree3.pojo.entity.Admin;
import com.tree3.service.AdminService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Admin表控制器
 *
 * @author rupert
 * @since 2024-03-08 09:27:41
 */
@Slf4j
@RequestMapping
@RestController
public class AdminController {


    private AdminService adminService;

    private RedisTemplate redisTemplate;

    @Autowired
    public AdminController(AdminService adminService, RedisTemplate redisTemplate) {
        this.adminService = adminService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/tokens")
    public Map<String, String> login(@RequestBody Admin admin, HttpSession session) {
        log.debug("接收到的admin对象为:{}", JSONUtils.toJsonStr(admin));
        if (admin == null || StringUtils.isEmpty(admin.getUsername()) || StringUtils.isEmpty(admin.getPassword())) {
            throw new RuntimeException("请填写用户名/密码!");
        }

        Map<String, String> result = new HashMap<>();

        Admin adminDB = adminService.login(admin);

        //登录成功,将sessionID作为token进行缓存
        String token = session.getId();

        //缓存token
        redisTemplate.opsForValue().set(RedisPrefix.TOKEN_KEY+token, adminDB, 30, TimeUnit.MINUTES);
        result.put("token", token);
        return result;
    }

    @GetMapping("/admin-user")
    public AdminDTO adminUser(String token) {
        log.debug("接收到的token:{}", token);
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("token未获取");
        }

        Admin adminRD = (Admin) redisTemplate.opsForValue().get(RedisPrefix.TOKEN_KEY+token);
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(adminRD, adminDTO);

        return adminDTO;
    }


    @DeleteMapping("/tokens/{token}")
    public void logout(@PathVariable("token") String token) {
        log.debug("接收到的token:{}", token);
        redisTemplate.delete(RedisPrefix.TOKEN_KEY+token);
    }


}
