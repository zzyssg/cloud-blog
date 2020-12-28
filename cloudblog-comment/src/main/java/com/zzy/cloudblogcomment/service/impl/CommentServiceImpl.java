package com.zzy.cloudblogcomment.service.impl;

import com.zzy.cloudblogcomment.dao.CommentMapper;
import com.zzy.cloudblogcomment.entity.Comment;
import com.zzy.cloudblogcomment.enums.ResponseEnum;
import com.zzy.cloudblogcomment.exception.CommonException;
import com.zzy.cloudblogcomment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author zzy
 * @Date 2020/12/23 15:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    @Override
    public Boolean addComment(Comment comment) {
        Integer result;
        try {
            //联表查询
//            Integer deleteLines = deleteCommentById(1);
            Integer insertLines = commentMapper.insertComment11(comment);
            if (insertLines == null || insertLines != 1) {
                log.error("新增评论时出错!");
                return false;
            }
            //comment拿到commentId,向中间表中插入数据
            Integer insertCommentRelativeLines = commentMapper.insertCommentRelative(comment);
            if (insertCommentRelativeLines == null || insertCommentRelativeLines != 1) {
                log.error("向评论表中间表中插入记录时出错!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("插入评论时出错:{}",e.getMessage());
            throw new CommonException(ResponseEnum.INSERT_COMMENT_ERROR.getCode(),
                    ResponseEnum.INSERT_COMMENT_ERROR.getMsg());
        }
        return true;
    }

    /**
     * 根据iD删除评论
     *
     * @param commentId
     * @return
     */
    @Override
    public Integer deleteCommentById(Integer commentId) {
        Integer integer = commentMapper.deleteCommentById(commentId);
        return integer;
    }
}
