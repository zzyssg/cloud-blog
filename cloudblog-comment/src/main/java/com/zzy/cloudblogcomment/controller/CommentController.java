package com.zzy.cloudblogcomment.controller;

import com.zzy.cloudblogcomment.entity.Comment;
import com.zzy.cloudblogcomment.enums.ResponseEnum;
import com.zzy.cloudblogcomment.exception.CommonException;
import com.zzy.cloudblogcomment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zzy
 * @Date 2020/12/23 14:51
 */
@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

    private final CommentService commentService;

    /**
     * 测试404
     */
    @GetMapping("/test")
    public void test() {
        log.info("success");
    }

    /**
     * 提交评论
     *
     * @param comment
     * @return
     */
    @PostMapping("/commit")
    public Boolean commit(@RequestBody Comment comment) {
//        ResponseBean result;
        //检查comment各参数是否合法
        if (!checkCommentLegal(comment)) {
            log.error("评论参数非法!参数为:{}", comment);
            throw new CommonException(ResponseEnum.COMMENT_PARAM_ERROR.getCode(),
                    ResponseEnum.COMMENT_PARAM_ERROR.getMsg());
        }
        //假如userId为-1，则前端是未登录的
        if (comment.getUserId().equals(-1)) {
            log.info("用户未登录!");
            return false;
        }
        commentService.addComment(comment);
        log.info("新增评论成功!评论:{}", comment);
        return true;
    }

    /**
     * 检查评论参数是否合法
     *
     * @param comment
     * @return
     */
    private Boolean checkCommentLegal(Comment comment) {
        //comment 的 userId blogId content不能为null
        Boolean result;
        result = comment != null
                && comment.getBlogId() != null
                && comment.getUserId() != null
                && comment.getContent() != null;
        return result;
    }


}
