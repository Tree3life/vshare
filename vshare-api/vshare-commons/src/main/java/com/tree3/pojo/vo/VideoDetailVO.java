package com.tree3.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tree3.pojo.entity.Comment;
import com.tree3.pojo.entity.User;
import lombok.*;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class VideoDetailVO {

    //视频id
    private Integer id;  //视频id
    private String title; //视频标题
    private String cover; //视频封面
    private String link;    //视频连接

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private String category;//视频类别

    @JsonProperty("plays_count")
    private Integer playsCount; //播放总数
    @JsonProperty("likes_count")
    private Integer likesCount; //点赞数

    private boolean liked;      //是否喜欢
    private boolean disliked;   //是否不喜欢
    private boolean favorite;   //是否收藏

    private User uploader;  //up主信息

    private List<Comment> comments;//评论信息

}
