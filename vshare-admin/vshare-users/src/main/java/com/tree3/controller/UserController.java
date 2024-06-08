package com.tree3.controller;

import com.tree3.pojo.entity.User;
import com.tree3.service.UserService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User表控制器
 *
 * @author rupert
 * @since 2024-03-10 09:32:25
 */
@Slf4j
@RequestMapping("users")
@RestController
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    public User queryById(@PathVariable("id") Integer id) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->queryById(Integer  " + id + ")");
        }

        return userService.queryById(id);
    }

    /**
     * 多条件选择查询
     *
     * @param ：可自主设置为Vo对象
     * @return 响应结果
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
            @RequestParam(name = "per_page", required = true, defaultValue = "5") Integer perPage,
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone
    ) {
        Map<String, Object> result = new HashMap<>(16);

        Integer offset = (page - 1) * perPage;
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setOffset(offset);
        user.setPageSize(perPage);
        log.debug(JSONUtils.toJsonStr(user));

        List<User> users = userService.queryAll(user);
        Integer count = userService.countAll(user);
        result.put("total_count", count);
        result.put("items", users);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/update")

    public User update(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->updateUser (" + user.toString() + ")");
        }

        return userService.update(user);
    }

    @DeleteMapping("/delete")

    public Boolean deleteById(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->deleteById ( Integer  " + user.getId() + ") ");
        }

        return userService.deleteById(user.getId());
    }

    @PostMapping("/save")

    public User save(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->saveUser (" + user.toString() + ")");
        }

        return userService.insert(user);
    }

}
