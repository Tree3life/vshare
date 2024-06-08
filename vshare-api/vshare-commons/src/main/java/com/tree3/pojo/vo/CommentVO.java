package com.tree3.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class CommentVO {

    private Integer id;

    private String content;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("parent_id")
    private Integer parentId;

    private Reviewer reviewer;

    @JsonProperty("sub_comments")
    private List<CommentVO> subComments;//子评论信息
}