package com.tree3.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户(User)表
 *
 * @author rupert
 * @since 2024-03-10 09:32:24
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 837456234186682346L;

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

    /**
     * 分页查询
     */
    private Integer offset;
    private Integer pageSize;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;


}

