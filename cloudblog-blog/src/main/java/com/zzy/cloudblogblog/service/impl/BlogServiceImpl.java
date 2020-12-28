package com.zzy.cloudblogblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzy.cloudblogblog.dao.BlogMapper;
import com.zzy.cloudblogblog.dao.BlogRocketmqTransactionLogMapper;
import com.zzy.cloudblogblog.dao.TypeMapper;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.entity.*;
import com.zzy.cloudblogblog.enums.ResponseEnum;
import com.zzy.cloudblogblog.exception.CommonException;
import com.zzy.cloudblogblog.feignclient.UserServiceFeignClient;
import com.zzy.cloudblogblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zzy
 * @Date 2020/12/2 16:52
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogServiceImpl implements BlogService {
    private final BlogMapper blogMapper;
    private final TypeMapper typeMapper;
    private final UserServiceFeignClient userServiceFeignClient;
    private final BlogRocketmqTransactionLogMapper blogRocketmqTransactionLogMapper;
    private final Source source;


    @Override
    public BlogDTO getBlogById(Integer blogId) {
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        //根据blog的userId查询出user
        //TODO 根据blog的userId获取user的详细信息，如username和avatar
        log.info("feign发送Http请求...");
        User user = userServiceFeignClient.getUserById(blog.getUser().getUserId());
        log.info("userDTO: {}", user);
        return BlogDTO.builder()
                .title(blog.getTitle())
                .blogId(blog.getBlogId())
                .typeId(blog.getType().getTypeId())
                .build();
    }


    @Override
    public Blog doWithUpdateBlog(Integer blogId) {
        //do something--某种业务
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        if (blog != null) {
            blog.setTitle("我是一个经过事务的blog.");
            //某种业务执行完毕，供下游使用，发送半消息
            String transactionId = UUID.randomUUID().toString();
            this.source.output().send(
                    MessageBuilder.withPayload(
                            BlogDTO.builder()
                                    .title("业务完成，发送给下游消息...")
                                    .build()
                    )
                            //多次setHeader，可以传参
                            .setHeader("transactionId", transactionId)
                            .setHeader("blogId", blog.getBlogId())
                            .setHeader("blog", JSON.toJSONString(blog))
                            .build()
            );
        } else {
            log.info("查询的blog不存在，无法给下游业务提供消息...");
        }
        return blog;
    }

    /**
     * 参数包含执行本地事务(LocalTransaction)需要的参数
     * 以及向中间事务表t_blog_rocketmq_transaction_log插入日志记录的id
     *
     * @param blog
     * @param transactionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateBlogWithRocketTransaction(Blog blog, String transactionId) {
        //本地事务 + 记录中间事务表 —— 两者同时成功或者失败，回查时本地事务是否成功的标志
        this.blogMapper.updateByPrimaryKeySelective(blog);
        this.blogRocketmqTransactionLogMapper.insertSelective(
                BlogRocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("本地事务在执行addBlog事务...")
                        .build()
        );
        return blog.toString();
    }

    /**
     * 查询所有博客
     *
     * @return
     */
    @Override
    public List<Blog> listAllBlogs() {
        List<Blog> allBlogs = blogMapper.listAllBlogs();
        if (allBlogs == null) {
            log.error("目前暂无博客!");
            throw new CommonException(ResponseEnum.BLOG_IS_NULL.getCode(),
                    ResponseEnum.BLOG_IS_NULL.getMsg());
        }
        return allBlogs;
    }

    /**
     * 查询具体某类型下的所有博客
     *
     * @param typeId
     * @return
     */
    @Override
    public List<Blog> listBlogsByTypeId(Long typeId) {
        List<Blog> result = new ArrayList<>();
        Type typeById = typeMapper.getTypeById(typeId.intValue());
        if (typeById == null) {
            log.error("目前还没有id为{}类型...",typeId);
            return result;
        }
        List<Blog> blogsByTypeId = blogMapper.listBlogsByTypeId(typeId);
        if (blogsByTypeId == null) {
            log.error("类型{}下的博客为空!", typeId);
            throw new CommonException(ResponseEnum.BLOG_OFTYPE_IS_NULL.getCode(),
                    ResponseEnum.BLOG_OFTYPE_IS_NULL.getMsg());
        }
        result = blogsByTypeId;
        return result;
    }

    /**
     * 按条件查询
     *
     * @param blog
     * @return
     */
    @Override
    public List<Blog> listBlogsByCondition(Blog blog) {
        return blogMapper.listBlogsByCondition(blog);
    }

    /**
     * 根据唯一标识查询博客
     *
     * @return
     */
    @Override
    public Blog getBlogById(Long blogId) {
        Blog blog = blogMapper.getBlogById(blogId);
        //从blog中取出comments
        List<Comment> resultComments = new ArrayList<>();
        List<Comment> comments = blog.getComments();
        if (!comments.isEmpty()) {
            //去除特殊情况：当blog中没有comment时，仍能查出来一条，但是其comment中的成员变量，除了blogId以外均为空
            //——因此去除这种特殊情况
            if (comments.size() == 1 && comments.get(0).getContent() == null) {
                comments.remove(0);
            }
            //此处处理一下comments
            //TODO 首先为comments赋user
            for (Comment comment : comments) {
                Integer commentUserId = comment.getUserId();
                //TODO 调用service接口，根据ID查询用户
                User curUser = userServiceFeignClient.getUserById(commentUserId);
//                User curUser = userService.getUserById(commentUserId);
                comment.setUser(curUser);
            }
            //找到各自的parentComment——设置父级评论昵称
            for (int i = 0; i < comments.size(); i++) {
                Comment sonComment = comments.get(i);
                if (sonComment.getParentCommentId() == null) {
                    continue;
                }
                for (int j = 0; j < comments.size(); j++) {
                    Comment fatherComment = comments.get(j);
                    if (sonComment.getParentCommentId().equals(fatherComment.getCommentId())) {
                        User parentCommentUser = User.builder()
                                .nickname(fatherComment.getUser().getNickname())
                                .avatar(fatherComment.getUser().getAvatar())
                                .build();
                        sonComment.setParentCommentUser(parentCommentUser);
                    }
                }
            }
            Map<Comment, List<Comment>> temComments = new HashMap<>();
            //先筛选出根评论，并且将根评论的id映射出一个map，key为根评论id，value为其子孙评论id
            for (Comment comment : comments) {
                if (comment.getParentCommentId() == null) {
                    //根评论
                    temComments.put(comment, new ArrayList<>());
                }
            }
            //遍历剩余评论，如果其parentId即为key的id或者 是key对应的value的id，将其扔进key对应的value里
            for (Comment comment : comments) {
                Integer curCommentParentId = comment.getParentCommentId();
                for (Map.Entry<Comment, List<Comment>> entry : temComments.entrySet()) {
                    Comment rootComment = entry.getKey();
                    List<Comment> subComments = entry.getValue();
                    Boolean isSubComment = curCommentParentId != null
                            && (curCommentParentId.equals(rootComment.getCommentId())
                            || hasParent(subComments,comment));
                    if (isSubComment) {
                        temComments.get(rootComment).add(comment);
                    }
                }
            }
            //处理一下temComments  key--comment有一个blogId
            //将key对应的value设置到replements
            for (Map.Entry<Comment, List<Comment>> entry : temComments.entrySet()) {
                Comment comment = entry.getKey();
                comment.setReplyComments(entry.getValue());
                resultComments.add(comment);
            }
            blog.setComments(resultComments);
        }
        return blog;
    }

    /**
     *如果子评论中有其直接父评论，也加入进此根评论的子评论中
     * @param subComments
     * @param comment
     * @return
     */
    private boolean hasParent(List<Comment> subComments, Comment comment) {
        for (Comment subComment : subComments) {
            if (subComment.getCommentId().equals(comment.getParentCommentId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据参数新增博客
     *
     * @param blog
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertBlog(Blog blog) {

        //向t_blog表中新增博客
        int effectedLine = blogMapper.insert(blog);
        if (effectedLine != 1) {
            log.error("新增博客时出错!");
            throw new CommonException(ResponseEnum.INSERT_BLOG_ERROR.getCode(),
                    ResponseEnum.INSERT_BLOG_ERROR.getMsg());
        }
        //向t_blog_type 和 t_blog_user 中新增博客
        try {
            blogMapper.insertRelativeBlog(blog);
//            throw new RuntimeException();
        } catch (Exception e) {
            log.error("blog表新增记录时，向其相关表中新增记录时出错!错误:{}", e.getMessage());
            throw new CommonException(ResponseEnum.INSERT_BLOG_RELATIVE_ERROR.getCode(),
                    ResponseEnum.INSERT_BLOG_RELATIVE_ERROR.getMsg());
        }
        return true;
    }

    /**
     * 根据参数更新一个博客
     *
     * @param blog
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateBlog(Blog blog) {
        try {

            int updateLine = blogMapper.updateByPrimaryKeySelective(blog);
            if (updateLine != 1) {
                log.error("更新博客时错误!更新的博客参数:{}", blog);
                throw new CommonException(ResponseEnum.UPDATE_BLOG_ERROR.getCode(),
                        ResponseEnum.UPDATE_BLOG_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("更新博客时发生错误:{}", e.getMessage());
        }
        log.info("更新博客成功!更新参数为:{}", blog);
        return true;

    }

    /**
     * 删除博客
     *
     * @param blog
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteBlog(Blog blog) {
        //删除博客表
        int deleteLine = blogMapper.deleteByPrimaryKey(blog);
        if (deleteLine != 1) {
            log.error(ResponseEnum.DELETE_BLOG_ERROR.getMsg());
            throw new CommonException(ResponseEnum.DELETE_BLOG_ERROR.getCode(),
                    ResponseEnum.DELETE_BLOG_ERROR.getMsg());
        }
        //删除博客关联表 t_blog_type t_blog_user
        blogMapper.deleteRelativeBlog(blog);
        return true;
    }

    /**
     * 查询某用户的所有博客
     *
     * @param userId
     * @return
     */
    @Override
    public List<Blog> listBlogsByUserId(Integer userId) {
        return blogMapper.listBlogsByUserId(userId);
    }

}
