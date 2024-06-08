package com.tree3.pojo.entity;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户(User)表
 *
 * @author rupert
 * @since 2024-03-13 12:10:44
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 162142697790049600L;

    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 头像链接
     */
    private String avatar;

    /**
     * 简介
     */
    private String intro;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 是否绑定手机号
     */
    private Boolean phoneLinked;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 是否绑定微信
     */
    private Boolean wechatLinked;

    /**
     * 关注数
     */
    private Integer followingCount;

    /**
     * 粉丝数
     */
    private Integer followersCount;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;



    //业务属性
    private String captcha;//短信验证码

}

