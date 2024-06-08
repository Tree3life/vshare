package com.tree3.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/8 19:43 </p>
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class AdminDTO {

    /**
     * 用户名
     */
    @JsonProperty("name")
    private String username;


    /**
     * 用户头像地址
     */
    private String avatar;
}
