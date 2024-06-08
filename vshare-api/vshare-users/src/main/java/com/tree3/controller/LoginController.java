package com.tree3.controller;

import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.vo.LoginVO;
import com.tree3.service.UserService;
import com.tree3.utils.ImageUtils;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/15 14:36 </p>
 */
@Slf4j
@RestController
public class LoginController {
    private final UserService userService;
    private StringRedisTemplate strRedisTemplate;
    private RedisTemplate redisTemplate;

    @Autowired
    public LoginController(UserService userService, StringRedisTemplate strRedisTemplate, RedisTemplate redisTemplate) {
        this.userService = userService;
        this.strRedisTemplate = strRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 手机端退出
     *
     * @param token
     * @return
     */
    @DeleteMapping("tokens")
    public ResponseEntity<Void> logout(String token) {
        log.debug("要退出的token：{}", token);
        //删除验证码相关内容
        String userKey = RedisPrefix.TOKEN_KEY + token;
        User user = (User) redisTemplate.opsForValue().get(userKey);

        if (!ObjectUtils.isEmpty(user)) {
            String phone = user.getPhone();
            strRedisTemplate.delete(RedisPrefix.PHONE_KEY + phone);
            strRedisTemplate.delete(RedisPrefix.TIMEOUT_KEY + phone);
        }
        //删除token
        strRedisTemplate.delete(userKey);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("tokens")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        log.debug("用户登录信息：{}", JSONUtils.toJsonStr(loginVO));

        String loginPhone = loginVO.getPhone();
        String codeDB = strRedisTemplate.opsForValue().get(RedisPrefix.PHONE_KEY + loginPhone);
        if (StringUtils.isEmpty(codeDB)) {
            throw new RuntimeException("验证码已失效");
        }

        if (!codeDB.equals(loginVO.getCaptcha())) {
            throw new RuntimeException("验证码有误");
        }

        /**
         * 根据电话查找数据库中的用户
         */
        User user = new User();
        user.setPhone(loginPhone);
        List<User> users = userService.queryAll(user);

        //未知用户/首次登录
        if (ObjectUtils.isEmpty(users)) {
            user = new User();//创建一个用户对象
            user.setName("xx");
            user.setCreatedAt(new Date());//设置创建时间
            user.setUpdatedAt(new Date());//设置更新时间
            user.setPhone(loginPhone); //设置用户的手机号
            user.setIntro("");//设置简介为空
            //初始化默认头像
            user.setAvatar(ImageUtils.getPhoto());//随机初始化头像
            user.setPhoneLinked(true);//是否绑定手机
            user.setWechatLinked(false);//是否绑定微信
            user.setFollowersCount(0);//设置粉丝数
            user.setFollowingCount(0);//设置关注数
            user.setIntro("xxx");
            user = userService.insert(user);//保存用户信息
        } else {
            if (users.size() > 1) {
                throw new RuntimeException("数据库用户异常-->匹配到多个用户！");
            }

            user = users.get(0);
        }

        //生成token
        String token = request.getSession().getId();
        String tokenKey = RedisPrefix.TOKEN_KEY + token;
        redisTemplate.opsForValue().set(tokenKey, user, 24, TimeUnit.HOURS);

        HashMap<String, Object> result = new HashMap<>();
        result.put("token", token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
