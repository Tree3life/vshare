package com.tree3.service.impl;

import com.tree3.client.UserClients;
import com.tree3.pojo.entity.Comment;
import com.tree3.dao.CommentMapper;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.vo.CommentVO;
import com.tree3.pojo.vo.Reviewer;
import com.tree3.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author rupert
 * @since 2024-03-17 21:03:55
 */
@Slf4j
@Transactional
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private CommentMapper commentMapper;

    private UserClients userClients;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, UserClients userClients) {
        this.commentMapper = commentMapper;
        this.userClients = userClients;
    }

    @Override
    public List<Comment> queryAll() {
        return commentMapper.queryAll();
    }

    @Override
    public List<Comment> queryAll(Comment comment) {
        return commentMapper.queryAll(comment);
    }

    @Override
    public Comment queryComment(Comment comment) {
        return commentMapper.queryComment(comment);
    }


    @Override
    public Comment queryById(Integer id) {
        return commentMapper.queryById(id);
    }

    @Override
    public Comment update(Comment comment) {
        this.commentMapper.update(comment);
        return queryById(comment.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return commentMapper.deleteById(id) > 0;
    }

    @Override
    public Comment insert(Comment comment) {
        this.commentMapper.insert(comment);
        return comment;
    }

    @Override
    public Map<String, Object> queryCommentByVideo(Integer videoId, Integer page, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        //根据视频id分页获取对应评论信息以及子评论信息
        List<CommentVO> commentVOList = new ArrayList<>();

        //1.根据视频id分页查询当前视频下评论内容
        Long total_counts = commentMapper.findByVideoIdCounts(videoId);
        result.put("total_count", total_counts);

        Comment comment = new Comment();
        comment.setVideoId(videoId);

        //根据 视频id 分页获取父评论信息
        List<Comment> comments = commentMapper.queryAllComment(comment, (page - 1) * pageSize, pageSize);
        if (!ObjectUtils.isEmpty(comments)) {
            commentVOList = comments.stream().map((item) -> {
                //将评论信息转为commentVO
                CommentVO commentVO = new CommentVO();
                //属性拷贝  commentvo id content createAt
                BeanUtils.copyProperties(item, commentVO);
                commentVO.setCreatedAt(item.getCreatedAt());

                //7.获取评论作者信息
                Reviewer reviewer = new Reviewer();
                //8.根据评论用户id查询用户信息
                User user = userClients.user(item.getUid());
                BeanUtils.copyProperties(user, reviewer);
                commentVO.setReviewer(reviewer);

                //11.设置子评论内容
                List<Comment> childComments = commentMapper.findByParentId(item.getId());
                List<CommentVO> childCommentVOS = getChildComments(childComments);
                //设置子评论
                commentVO.setSubComments(childCommentVOS);
                return commentVO;
            }).collect(Collectors.toList());
        }

        result.put("items", commentVOList);
        return result;
    }

    private List<CommentVO> getChildComments(List<Comment> childComments) {
        List<CommentVO> collect = new ArrayList<>();
        if (!ObjectUtils.isEmpty(childComments)) {
            collect = childComments.stream().map(commentChild -> {
                CommentVO commentChildVO = new CommentVO();
                BeanUtils.copyProperties(commentChild, commentChildVO);
                commentChildVO.setCreatedAt(commentChild.getCreatedAt());
                //12.设置评论用户信息
                User userChild = userClients.user(commentChild.getUid());
                Reviewer reviewerChild = new Reviewer();
                BeanUtils.copyProperties(userChild, reviewerChild);
                commentChildVO.setReviewer(reviewerChild);
                return commentChildVO;
            }).collect(Collectors.toList());
        }
        return collect;
    }
}
