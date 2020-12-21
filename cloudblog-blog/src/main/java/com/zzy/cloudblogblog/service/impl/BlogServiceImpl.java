package com.zzy.cloudblogblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.zzy.cloudblogblog.dao.blog.BlogMapper;
import com.zzy.cloudblogblog.dao.midtransaction.BlogRocketmqTransactionLogMapper;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.dto.UserDTO;
import com.zzy.cloudblogblog.entity.blog.Blog;
import com.zzy.cloudblogblog.entity.midtransaction.BlogRocketmqTransactionLog;
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

import java.util.List;
import java.util.UUID;

/**
 * @author zzy
 * @Date 2020/12/2 16:52
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogServiceImpl implements BlogService {
    private final BlogMapper blogMapper;
    private final UserServiceFeignClient userServiceFeignClient;
    private final BlogRocketmqTransactionLogMapper blogRocketmqTransactionLogMapper;
    private final Source source;


    @Override
    public BlogDTO queryById(Integer blogId) {
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        //根据blog的userId查询出user
        //TODO 根据blog的userId获取user的详细信息，如username和avatar
        log.info("feign发送Http请求...");
        UserDTO userDTO = userServiceFeignClient.findById(blog.getUserId());
        log.info("userDTO: {}", userDTO);
        return BlogDTO.builder()
                .title(blog.getTitle())
                .blogId(blog.getBlogId())
                .typeId(blog.getTypeId())
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
        return blogMapper.listAllBlogs();
    }

    /**
     * 查询具体某类型下的所有博客
     *
     * @param typeId
     * @return
     */
    @Override
    public List<Blog> listBlogsByTypeId(Long typeId) {
        return blogMapper.listBlogsByTypeId(typeId);
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
        return blogMapper.selectByPrimaryKey(blogId);
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

        int effectedLine = blogMapper.insert(blog);
        if (effectedLine != 1) {
            log.error("新增博客时出错!");
            throw new CommonException(ResponseEnum.INSERT_BLOG_ERROR.getCode(),
                    ResponseEnum.INSERT_BLOG_ERROR.getMsg());
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
        int updateLine = blogMapper.updateByPrimaryKeySelective(blog);
        if (updateLine != 1) {
            log.error("更新博客时错误!更新的博客参数:{}", blog);
            throw new CommonException(ResponseEnum.UPDATE_BLOG_ERROR.getCode(),
                    ResponseEnum.UPDATE_BLOG_ERROR.getMsg());
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
        int deleteLine = blogMapper.deleteByPrimaryKey(blog);
        if (deleteLine != 1) {
            log.error(ResponseEnum.DELETE_BLOG_ERROR.getMsg());
            throw new CommonException(ResponseEnum.DELETE_BLOG_ERROR.getCode(),
                    ResponseEnum.DELETE_BLOG_ERROR.getMsg());
        }
        return true;
    }

}
