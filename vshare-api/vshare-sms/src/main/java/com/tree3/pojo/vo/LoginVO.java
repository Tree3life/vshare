package com.tree3.pojo.vo;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class LoginVO implements Serializable {
    private String phone;  //用来接收手机号

    private String captcha; //接收验证码
}