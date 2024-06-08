package com.tree3.pojo.entity;

import lombok.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/10 20:58 </p>
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VideoDTO extends Video{

    /**
     * 分页相关
     */
    private Integer offset;
    private Integer pageSize;


    private String userName;
    private String category;

    private User uploader;
}
