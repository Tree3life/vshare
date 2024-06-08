package com.tree3.controller;

import com.tree3.constants.RedisPrefix;
import com.tree3.pojo.vo.LoginVO;
import com.tree3.utils.JSONUtils;
import com.tree3.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 验证码发送
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/13 14:47 </p>
 */
@Slf4j
@RequestMapping
@RestController
public class CaptchasController {

    private StringRedisTemplate stringRedisTemplate;

    private SMSUtils smsUtils;

    @Autowired
    public CaptchasController(StringRedisTemplate stringRedisTemplate, SMSUtils smsUtils) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.smsUtils = smsUtils;
    }


    @PostMapping("/captchas")
    public ResponseEntity<Void> sendCaptchas(@RequestBody LoginVO loginVO) {
        log.info("发送短信的手机号为: {}", JSONUtils.toJsonStr(loginVO));

        //1.获取接收到的手机号
        String phone = loginVO.getPhone();

        //2.每次发送验证码之前判断,是否存在
        String timeoutKey = RedisPrefix.TIMEOUT_KEY + phone;
        if (stringRedisTemplate.hasKey(timeoutKey)) {
            throw new RuntimeException("提示: 不允许重复发送!");
        }

        try {
            //3.生成4位随机字符
            String code = RandomStringUtils.randomNumeric(4);
            log.info("发送的验证码: {}", code);
            //4.todo 根据接收手机号以及生成随机字符 发送验证码
//            smsUtils.sendMsg(phone, code);

            //5.将验证码放入redis   key: phone_132....   value:code
            String phoneKey = RedisPrefix.PHONE_KEY + phone;
            //10分钟内验证有效
            stringRedisTemplate.opsForValue().set(phoneKey, code, 10, TimeUnit.MINUTES);

            //6.用以实现`如果验证码在有效期内,不允许重新发送`  //timeout_132... true
            stringRedisTemplate.opsForValue().set(timeoutKey, "true", 60, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
